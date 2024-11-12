package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import service.CuentaService;

public class CuentaGUI {

    private static CuentaService cuentaService = new CuentaService();
    private static JTextArea accountDetailsTextArea;

    public static void placeGestionCuentasComponents(JInternalFrame frame) {
        // Crear el panel
        JPanel panel = new JPanel();
        panel.setLayout(null);  // Establecer layout nulo para el control total de los tamaños y posiciones
        frame.add(panel);

        // Botón para crear cuenta
        JButton createAccountButton = new JButton("Crear Cuenta");
        createAccountButton.setBounds(10, 20, 160, 25);
        panel.add(createAccountButton);
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cuentaService.createAccount();  // Crear cuenta
                updateAccountList();  // Actualizar la lista de cuentas
            }
        });

        // Botón para listar cuentas (Inicialización de JTextArea y JScrollPane)
        accountDetailsTextArea = new JTextArea();
        accountDetailsTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(accountDetailsTextArea);
        scrollPane.setBounds(10, 200, 300, 200);
        panel.add(scrollPane);

        JButton listAccountsButton = new JButton("Listar Cuentas");
        listAccountsButton.setBounds(10, 50, 160, 25);
        panel.add(listAccountsButton);

        listAccountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAccountList();  // Actualizar la lista de cuentas
            }
        });

        // Botón para actualizar cuenta
        JButton updateAccountButton = new JButton("Actualizar Cuenta");
        updateAccountButton.setBounds(10, 80, 160, 25);
        panel.add(updateAccountButton);

        updateAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cuentaService.updateAccount();  // Actualizar cuenta
                updateAccountList();  // Actualizar la lista de cuentas
            }
        });

        // Botón para eliminar cuenta
        JButton deleteAccountButton = new JButton("Eliminar Cuenta");
        deleteAccountButton.setBounds(10, 110, 160, 25);
        panel.add(deleteAccountButton);

        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cuentaService.deleteAccount();  // Eliminar cuenta
                updateAccountList();  // Actualizar la lista de cuentas
            }
        });

        JButton generarReporteButton = new JButton("Generar Reporte Movimientos");
        generarReporteButton.setBounds(10, 140, 200, 25);
        panel.add(generarReporteButton);

        generarReporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int cuentaId = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la cuenta"));
                    int mes = Integer.parseInt(JOptionPane.showInputDialog("Ingrese mes (1-12)"));
                    int anio = Integer.parseInt(JOptionPane.showInputDialog("Ingrese año"));

                    cuentaService.generarReporteMovimientos(cuentaId, mes, anio);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton generarInteresesButton = new JButton("Generar Intereses");
        generarInteresesButton.setBounds(10, 170, 160, 25);
        panel.add(generarInteresesButton);

        generarInteresesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cuentaService.generarIntereses();  // Generar intereses
                updateAccountList();
            }
        });

        // Cargar la lista de cuentas al iniciar la interfaz
        updateAccountList();
    }

    // Método para actualizar la lista de cuentas en el JTextArea
    private static void updateAccountList() {
        StringBuilder sb = new StringBuilder();
        cuentaService.listAccounts(sb);  // Obtener las cuentas y llenar el StringBuilder
        accountDetailsTextArea.setText(sb.toString());  // Actualizar el JTextArea con la lista actualizada
    }
}
