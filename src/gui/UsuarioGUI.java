package gui;

import javax.swing.*;
import service.UsuarioService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UsuarioGUI {
    private static UsuarioService usuarioService = new UsuarioService();
    private static JTextArea userDetailsTextArea;

    public static void placeGestionUsuariosComponents(JInternalFrame frame) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Botón Crear Usuario
        JButton createUserButton = new JButton("Crear Usuario");
        styleButton(createUserButton);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(createUserButton, gbc);
        createUserButton.addActionListener(e -> usuarioService.createUser());

        // Botón Listar Usuarios
        JButton listUsersButton = new JButton("Listar Usuarios");
        styleButton(listUsersButton);
        gbc.gridy = 1;
        panel.add(listUsersButton, gbc);

        listUsersButton.addActionListener(e -> {
            List<String> listUser = usuarioService.listUsers();
            StringBuilder sb = new StringBuilder();
            if (listUser != null && !listUser.isEmpty()) {
                listUser.forEach(usuario -> sb.append(usuario).append("\n"));
                userDetailsTextArea.setText(sb.toString());
            } else {
                JOptionPane.showMessageDialog(panel, "No hay usuarios registrados.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Botón Actualizar Usuario
        JButton updateUserButton = new JButton("Actualizar Usuario");
        styleButton(updateUserButton);
        gbc.gridy = 2;
        panel.add(updateUserButton, gbc);
        updateUserButton.addActionListener(e -> usuarioService.updateUser());

        // Botón Eliminar Usuario
        JButton deleteUserButton = new JButton("Eliminar Usuario");
        styleButton(deleteUserButton);
        gbc.gridy = 3;
        panel.add(deleteUserButton, gbc);
        deleteUserButton.addActionListener(e -> usuarioService.deleteUser());

        // Botón Buscar Usuario por ID
        JButton searchUserButton = new JButton("Buscar Usuario por ID");
        styleButton(searchUserButton);
        gbc.gridy = 4;
        panel.add(searchUserButton, gbc);
        searchUserButton.addActionListener(e -> usuarioService.searchUserById());
    }

    // Método para estilizar botones
    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
    }
}
