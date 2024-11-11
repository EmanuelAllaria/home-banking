package gui;

import javax.swing.*;

import service.TarjetaService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import service.TarjetaService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TarjetaGUI {
    private static TarjetaService tarjetaService = new TarjetaService();

    public static void placeGestionTarjetasComponents(JInternalFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        JButton createCardButton = new JButton("Crear Tarjeta");
        createCardButton.setBounds(10, 20, 160, 25);
        panel.add(createCardButton);

        createCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tarjetaService.createCard();
            }
        });

        JButton updateCardButton = new JButton("Actualizar Tarjeta");
        updateCardButton.setBounds(10, 50, 160, 25);
        panel.add(updateCardButton);

        updateCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tarjetaService.updateCard();
            }
        });

        JButton deleteCardButton = new JButton("Eliminar Tarjeta");
        deleteCardButton.setBounds(10, 80, 160, 25);
        panel.add(deleteCardButton);

        deleteCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tarjetaService.deleteCard();
            }
        });

        JButton listCardsButton = new JButton("Listar Tarjetas");
        listCardsButton.setBounds(10, 110, 160, 25);
        panel.add(listCardsButton);

        listCardsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tarjetaService.listCards();
            }
        });
    }
}

