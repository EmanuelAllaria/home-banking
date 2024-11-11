package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import service.ResumenService;

public class ResumenGUI {

    private static ResumenService resumenService = new ResumenService();
    private static JTextArea resumenDetailsTextArea;

    public static void placeGestionResumenComponents(JInternalFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        resumenDetailsTextArea = new JTextArea();
        resumenDetailsTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resumenDetailsTextArea);
        scrollPane.setBounds(10, 160, 300, 200);
        panel.add(scrollPane);

        JButton listResumenesButton = new JButton("Listar Resúmenes");
        listResumenesButton.setBounds(10, 20, 160, 25);
        panel.add(listResumenesButton);

        listResumenesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateResumenList();  // Actualizar la lista de resúmenes
            }
        });

        // Botón para ver resumen detallado por cuenta
        JButton viewResumenByCuentaButton = new JButton("Ver Resumen por Cuenta");
        viewResumenByCuentaButton.setBounds(10, 50, 160, 25);
        panel.add(viewResumenByCuentaButton);

        viewResumenByCuentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cuentaIdStr = JOptionPane.showInputDialog("Ingrese el ID de la cuenta:");
                try {
                    int cuentaId = Integer.parseInt(cuentaIdStr);
                    updateResumenByCuenta(cuentaId);  // Actualizar el resumen de una cuenta específica
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "ID de cuenta inválido");
                }
            }
        });

        // Cargar la lista de resúmenes al iniciar la interfaz
        updateResumenList();
    }

    // Método para actualizar la lista completa de resúmenes en el JTextArea
    private static void updateResumenList() {
        StringBuilder sb = new StringBuilder();
        resumenService.listResumenes(sb);  // Obtener los resúmenes y llenar el StringBuilder
        resumenDetailsTextArea.setText(sb.toString());  // Actualizar el JTextArea con la lista de resúmenes
    }

    // Método para mostrar el resumen de una cuenta específica
    private static void updateResumenByCuenta(int cuentaId) {
        StringBuilder sb = new StringBuilder();
        resumenService.getResumenByCuenta(cuentaId, sb);  // Obtener resumen específico y llenar el StringBuilder
        resumenDetailsTextArea.setText(sb.toString());  // Actualizar el JTextArea con el resumen de la cuenta específica
    }
}
