package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import service.CuentaService;
import javax.swing.border.TitledBorder;

public class CuentaGUI {

    private static CuentaService cuentaService = new CuentaService();
    private static JTextArea accountDetailsTextArea;

    public static void placeGestionCuentasComponents(JInternalFrame frame) {
        // Configurar el panel principal con GridBagLayout para un diseño moderno
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Botón para crear cuenta
        JButton createAccountButton = new JButton("Crear Cuenta");
        styleButton(createAccountButton);
        gbc.gridy = 0;
        panel.add(createAccountButton, gbc);
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cuentaService.createAccount();
                updateAccountList();
            }
        });

        // Botón para listar cuentas
        JButton listAccountsButton = new JButton("Listar Cuentas");
        styleButton(listAccountsButton);
        gbc.gridy = 1;
        panel.add(listAccountsButton, gbc);
        listAccountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAccountList();
            }
        });

        // Botón para actualizar cuenta
        JButton updateAccountButton = new JButton("Actualizar Cuenta");
        styleButton(updateAccountButton);
        gbc.gridy = 2;
        panel.add(updateAccountButton, gbc);
        updateAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cuentaService.updateAccount();
                updateAccountList();
            }
        });

        // Botón para eliminar cuenta
        JButton deleteAccountButton = new JButton("Eliminar Cuenta");
        styleButton(deleteAccountButton);
        gbc.gridy = 3;
        panel.add(deleteAccountButton, gbc);
        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cuentaService.deleteAccount();
                updateAccountList();
            }
        });

        // Botón para generar reporte de movimientos
        JButton generarReporteButton = new JButton("Generar Reporte Movimientos");
        styleButton(generarReporteButton);
        gbc.gridy = 4;
        panel.add(generarReporteButton, gbc);
        generarReporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int cuentaId = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la cuenta"));
                    int mes = Integer.parseInt(JOptionPane.showInputDialog("Ingrese mes (1-12)"));
                    int anio = Integer.parseInt(JOptionPane.showInputDialog("Ingrese año"));
                    cuentaService.generarReporteMovimientos(cuentaId, mes, anio);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Botón para generar intereses
        JButton generarInteresesButton = new JButton("Generar Intereses");
        styleButton(generarInteresesButton);
        gbc.gridy = 5;
        panel.add(generarInteresesButton, gbc);
        generarInteresesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cuentaService.generarIntereses();
                updateAccountList();
            }
        });

        // Área de texto para mostrar detalles de las cuentas
        accountDetailsTextArea = new JTextArea();
        accountDetailsTextArea.setEditable(false);
        accountDetailsTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(accountDetailsTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "Detalles de Cuentas",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Color.BLUE
        ));
        scrollPane.setPreferredSize(new Dimension(400, 200));
        panel.setLayout(new FlowLayout());
        panel.add(scrollPane);

        // Cargar la lista de cuentas al iniciar la interfaz
        updateAccountList();
    }

    // Método para estilizar botones
    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // Método para actualizar la lista de cuentas en el JTextArea
    private static void updateAccountList() {
        StringBuilder sb = new StringBuilder();
        cuentaService.listAccounts(sb);
        accountDetailsTextArea.setText(sb.toString());
    }
}
