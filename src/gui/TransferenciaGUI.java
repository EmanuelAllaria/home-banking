package gui;

import javax.swing.*;
import clases.*;
import service.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class TransferenciaGUI {
    private static TransferenciaService transferenciaService = new TransferenciaService();
    private static CuentaService cuentaService = new CuentaService();
    private static JTextArea transferDetailsArea;

    public static void placeTransferenciasComponents(JInternalFrame frame) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Etiqueta Usuario
        JLabel userLabel = new JLabel("Usuario:");
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        // ComboBox Usuario
        JComboBox<String> userComboBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(userComboBox, gbc);

        List<Usuario> usuarios;
        try {
            UsuarioService usuarioService = new UsuarioService();
            usuarios = usuarioService.listUsersForTransferencia();
            if (usuarios != null) {
                for (Usuario usuario : usuarios) {
                    userComboBox.addItem("Usuario ID: " + usuario.getId() + ", Nombre: " + usuario.getNombre());
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron usuarios disponibles.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener la lista de usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Etiqueta Cuenta Origen
        JLabel sourceAccountLabel = new JLabel("Cuenta Origen:");
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(sourceAccountLabel, gbc);

        // ComboBox Cuenta Origen
        JComboBox<String> sourceAccountComboBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(sourceAccountComboBox, gbc);

        // Etiqueta Cuenta Destino
        JLabel destinationAccountLabel = new JLabel("Cuenta Destino:");
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(destinationAccountLabel, gbc);

        // ComboBox Cuenta Destino
        JComboBox<String> destinationAccountComboBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(destinationAccountComboBox, gbc);

        // Etiqueta Monto
        JLabel amountLabel = new JLabel("Monto:");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(amountLabel, gbc);

        // Campo de texto Monto
        JTextField amountField = new JTextField();
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(amountField, gbc);

        // Área de texto para detalles
        transferDetailsArea = new JTextArea(10, 30);
        transferDetailsArea.setEditable(false);
        transferDetailsArea.setBorder(BorderFactory.createTitledBorder("Detalles de Transferencia"));
        JScrollPane scrollPane = new JScrollPane(transferDetailsArea);
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);

        // Botón Transferir
        JButton transferButton = new JButton("Transferir");
        styleButton(transferButton);
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(transferButton, gbc);

        // Eventos
        userComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuarioSeleccionado = (String) userComboBox.getSelectedItem();
                String id = usuarioSeleccionado.split(",")[0].replace("Usuario ID: ", "").trim();
                int usuarioId = Integer.parseInt(id);

                sourceAccountComboBox.removeAllItems();
                destinationAccountComboBox.removeAllItems();

                try {
                    List<Cuenta> cuentas = cuentaService.listarCuentasPorUsuarioId(usuarioId);
                    for (Cuenta cuenta : cuentas) {
                        sourceAccountComboBox.addItem(String.valueOf(cuenta));
                    }

                    cuentas = cuentaService.listarCuentasSinUsuarioId(usuarioId);
                    for (Cuenta cuenta : cuentas) {
                        destinationAccountComboBox.addItem(String.valueOf(cuenta));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Extraer solo el número del ID de la cuenta origen
                    String cuentaOrigenSeleccionada = (String) sourceAccountComboBox.getSelectedItem();
                    System.out.println(cuentaOrigenSeleccionada);
                    int idOrigen = Integer.parseInt(cuentaOrigenSeleccionada.split(",")[0].replace("ID Cuenta: ", "").trim());

                    // Extraer solo el número del ID de la cuenta destino
                    String cuentaDestinoSeleccionada = (String) destinationAccountComboBox.getSelectedItem();
                    int idDestino = Integer.parseInt(cuentaDestinoSeleccionada.split(",")[0].replace("ID Cuenta: ", "").trim());

                    // Obtener el monto
                    double monto = Double.parseDouble(amountField.getText());

                    // Llamar al servicio de transferencia
                    String result = transferenciaService.realizarTransferencia(new Transferencia(idOrigen, idDestino, monto));
                    transferDetailsArea.setText(result);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 153, 76));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}
