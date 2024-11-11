package gui;


import javax.swing.*;

import clases.Cuenta;
import clases.Transferencia;
import service.CuentaService;
import service.TransferenciaService;
import service.UsuarioService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.*;
import service.CuentaService;
import service.TransferenciaService;
import service.UsuarioService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class TransferenciaGUI {
    private static TransferenciaService transferenciaService = new TransferenciaService();
    private static CuentaService cuentaService = new CuentaService();

    public static void placeTransferenciasComponents(JInternalFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JComboBox<String> userComboBox = new JComboBox<>();
        userComboBox.setBounds(100, 20, 160, 25);
        panel.add(userComboBox);

        List<String> usuarios;
        try {
            usuarios = UsuarioService.listUsers();
            for (String usuario : usuarios) {
                userComboBox.addItem(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel sourceAccountLabel = new JLabel("Cuenta Origen:");
        sourceAccountLabel.setBounds(10, 50, 100, 25);
        panel.add(sourceAccountLabel);

        JComboBox<String> sourceAccountComboBox = new JComboBox<>();
        sourceAccountComboBox.setBounds(120, 50, 160, 25);
        panel.add(sourceAccountComboBox);

        JLabel destinationAccountLabel = new JLabel("Cuenta Destino:");
        destinationAccountLabel.setBounds(10, 80, 100, 25);
        panel.add(destinationAccountLabel);

        JComboBox<String> destinationAccountComboBox = new JComboBox<>();
        destinationAccountComboBox.setBounds(120, 80, 160, 25);
        panel.add(destinationAccountComboBox);

        JLabel amountLabel = new JLabel("Monto:");
        amountLabel.setBounds(10, 110, 100, 25);
        panel.add(amountLabel);

        JTextField amountField = new JTextField();
        amountField.setBounds(120, 110, 160, 25);
        panel.add(amountField);

        JTextArea transferDetailsArea = new JTextArea();
        transferDetailsArea.setBounds(10, 140, 270, 100);
        transferDetailsArea.setEditable(false);
        panel.add(transferDetailsArea);

        JButton transferButton = new JButton("Transferir");
        transferButton.setBounds(10, 250, 160, 25);
        panel.add(transferButton);

        // Llenar el sourceAccountComboBox cuando se seleccione un usuario
        userComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuarioSeleccionado = (String) userComboBox.getSelectedItem();
                String id = usuarioSeleccionado.split(",")[0].replace("ID: ", "").trim();
                int usuarioId = Integer.parseInt(id);
                sourceAccountComboBox.removeAllItems();
                List<Cuenta> cuentas = null;
                try {
                    cuentas = cuentaService.listarCuentasPorUsuarioId(usuarioId);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                for (Cuenta cuenta : cuentas) {
                    sourceAccountComboBox.addItem(String.valueOf(cuenta));
                }
            }
        });

        // Llenar el sourceAccountComboBox cuando se seleccione un usuario
        userComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuarioSeleccionado = (String) userComboBox.getSelectedItem();
                String id = usuarioSeleccionado.split(",")[0].replace("ID: ", "").trim();
                int usuarioId = Integer.parseInt(id);
                destinationAccountComboBox.removeAllItems();
                List<Cuenta> cuentas = null;
                try {
                    cuentas = cuentaService.listarCuentasSinUsuarioId(usuarioId);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                for (Cuenta cuenta : cuentas) {
                    destinationAccountComboBox.addItem(String.valueOf(cuenta));
                }
            }
        });

        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Extrae solo el ID de la cuenta de origen
                    String cuentaOrigenSeleccionada = (String) sourceAccountComboBox.getSelectedItem();
                    String idOrigenStr = cuentaOrigenSeleccionada.split(",")[0].replace("ID: ", "").trim();
                    int idOrigen = Integer.parseInt(idOrigenStr);

                    // Extrae solo el ID de la cuenta destino
                    String cuentaDestino = (String) destinationAccountComboBox.getSelectedItem();
                    String idDestinoStr = cuentaDestino.split(",")[0].replace("ID: ", "").trim();
                    int idDestino = Integer.parseInt(idDestinoStr);

                    // Convierte el monto
                    double monto = Double.parseDouble(amountField.getText());

                    // Llama al servicio de transferencia con los IDs procesados
                    String result = transferenciaService.realizarTransferencia(new Transferencia(idOrigen, idDestino, monto));
                    transferDetailsArea.setText(result);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Por favor ingrese valores válidos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}


