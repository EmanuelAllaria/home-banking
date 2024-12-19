package gui;


import javax.swing.*;

import service.UsuarioService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UsuarioGUI {
    private static UsuarioService usuarioService = new UsuarioService();

    public static void placeGestionUsuariosComponents(JInternalFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        JButton createUserButton = new JButton("Crear Usuario");
        createUserButton.setBounds(10, 20, 160, 25);
        panel.add(createUserButton);

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                usuarioService.createUser();
            }
        });

        JButton listUsersButton = new JButton("Listar Usuarios");
        listUsersButton.setBounds(10, 50, 160, 25);
        panel.add(listUsersButton);

        listUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> listUser = usuarioService.listUsers();
                StringBuilder sb = new StringBuilder();
                listUser.forEach(usuario -> sb.append(usuario + "\n"));
                JOptionPane.showMessageDialog(null, sb.toString(), "Lista de Usuarios", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton updateUserButton = new JButton("Actualizar Usuario");
        updateUserButton.setBounds(10, 80, 160, 25);
        panel.add(updateUserButton);

        updateUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                usuarioService.updateUser();
            }
        });

        JButton deleteUserButton = new JButton("Eliminar Usuario");
        deleteUserButton.setBounds(10, 110, 160, 25);
        panel.add(deleteUserButton);

        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                usuarioService.deleteUser();
            }
        });

        JButton searchUserButton = new JButton("Buscar Usuario por ID");
        searchUserButton.setBounds(10, 140, 160, 25);
        panel.add(searchUserButton);

        searchUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                usuarioService.searchUserById();
            }
        });
    }
}
