package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import service.ResumenService;

public class ResumenGUI {

    private static ResumenService resumenService = new ResumenService();
    private static JTextArea resumenDetailsTextArea;

    public static void placeGestionResumenComponents(JInternalFrame frame) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Botón para listar resúmenes
        JButton listResumenesButton = new JButton("Listar Resúmenes");
        styleButton(listResumenesButton);
        gbc.gridy = 0;
        panel.add(listResumenesButton, gbc);

        listResumenesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateResumenList();
            }
        });

        // Botón para ver resumen por cuenta
        JButton viewResumenByCuentaButton = new JButton("Ver Resumen por Cuenta");
        styleButton(viewResumenByCuentaButton);
        gbc.gridy = 1;
        panel.add(viewResumenByCuentaButton, gbc);

        viewResumenByCuentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cuentaIdStr = JOptionPane.showInputDialog("Ingrese el ID de la cuenta:");
                try {
                    int cuentaId = Integer.parseInt(cuentaIdStr);
                    updateResumenByCuenta(cuentaId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "ID de cuenta inválido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Área de texto para mostrar resúmenes
        resumenDetailsTextArea = new JTextArea(10, 30);
        resumenDetailsTextArea.setEditable(false);
        resumenDetailsTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(resumenDetailsTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Detalles de Resúmenes"));
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        // Cargar la lista de resúmenes al iniciar
        updateResumenList();
    }

    private static void updateResumenList() {
        StringBuilder sb = new StringBuilder();
        resumenService.listarResumenes(sb);
        resumenDetailsTextArea.setText(sb.toString());
    }

    private static void updateResumenByCuenta(int cuentaId) {
        StringBuilder sb = new StringBuilder();
        resumenService.obtenerResumenPorCuenta(cuentaId, sb);
        resumenDetailsTextArea.setText(sb.toString());
    }

    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
