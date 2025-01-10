package gui;

import javax.swing.*;
import service.TarjetaService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TarjetaGUI {
    private static TarjetaService tarjetaService = new TarjetaService();
    private static JTextArea cardDetailsTextArea;

    public static void placeGestionTarjetasComponents(JInternalFrame frame) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Botón para crear tarjeta
        JButton createCardButton = new JButton("Crear Tarjeta");
        styleButton(createCardButton);
        gbc.gridy = 0;
        panel.add(createCardButton, gbc);

        createCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tarjetaService.createCard();
                updateCardList();
            }
        });

        // Botón para actualizar tarjeta
        JButton updateCardButton = new JButton("Actualizar Tarjeta");
        styleButton(updateCardButton);
        gbc.gridy = 1;
        panel.add(updateCardButton, gbc);

        updateCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tarjetaService.updateCard();
                updateCardList();
            }
        });

        // Botón para eliminar tarjeta
        JButton deleteCardButton = new JButton("Eliminar Tarjeta");
        styleButton(deleteCardButton);
        gbc.gridy = 2;
        panel.add(deleteCardButton, gbc);

        deleteCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tarjetaService.deleteCard();
                updateCardList();
            }
        });

        // Botón para listar tarjetas
        JButton listCardsButton = new JButton("Listar Tarjetas");
        styleButton(listCardsButton);
        gbc.gridy = 3;
        panel.add(listCardsButton, gbc);

        listCardsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCardList();
            }
        });

        // Botón para registrar débito
        JButton registrarDebitoButton = new JButton("Registrar Débito");
        styleButton(registrarDebitoButton);
        gbc.gridy = 4;
        panel.add(registrarDebitoButton, gbc);

        registrarDebitoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tarjetaService.registrarDebito();
                updateCardList();
            }
        });

        // Botón para emitir resumen
        JButton emitirResumenButton = new JButton("Emitir Resumen");
        styleButton(emitirResumenButton);
        gbc.gridy = 5;
        panel.add(emitirResumenButton, gbc);

        emitirResumenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tarjetaService.emitirResumenMensual();
            }
        });

        // Área de texto para mostrar detalles
        cardDetailsTextArea = new JTextArea(10, 30);
        cardDetailsTextArea.setEditable(false);
        cardDetailsTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(cardDetailsTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Detalles de Tarjetas"));
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

    }

    // Método para actualizar la lista de tarjetas en el JTextArea
    private static void updateCardList() {
        StringBuilder sb = new StringBuilder();
        tarjetaService.listCards();
        cardDetailsTextArea.setText(sb.toString());
    }

    // Estilizar los botones para un diseño moderno
    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
