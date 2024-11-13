package service;

import javax.swing.*;
import clases.Usuario;
import dao.UsuarioDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class UsuarioService {
    private static List<String> usuarios;

    public void createUser() {
        try {
            String nombre = JOptionPane.showInputDialog("Ingrese nombre del usuario");
            double saldo = Double.parseDouble(JOptionPane.showInputDialog("Ingrese saldo inicial"));

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setSaldo(saldo);

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.crearUsuario(usuario);

            JOptionPane.showMessageDialog(null, "Usuario creado con éxito!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> listUsers() {
        List<String> usuariosList = new ArrayList<>();
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<Usuario> usuarios = usuarioDAO.listarUsuarios();
            StringBuilder sb = new StringBuilder();

            for (Usuario usuario : usuarios) {
                String usuarioInfo = "ID: " + usuario.getId() +
                        ", Nombre: " + usuario.getNombre() +
                        ", Saldo: " + usuario.getSaldo();
                usuariosList.add(usuarioInfo);
                sb.append(usuarioInfo).append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuariosList;
    }

    public void updateUser() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID del usuario a actualizar"));
            String nombre = JOptionPane.showInputDialog("Ingrese nuevo nombre del usuario");
            double saldo = Double.parseDouble(JOptionPane.showInputDialog("Ingrese nuevo saldo"));

            Usuario usuario = new Usuario();
            usuario.setId(id);
            usuario.setNombre(nombre);
            usuario.setSaldo(saldo);

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.actualizarUsuario(usuario);

            JOptionPane.showMessageDialog(null, "Usuario actualizado con éxito!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID del usuario a eliminar"));

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.eliminarUsuario(id);

            JOptionPane.showMessageDialog(null, "Usuario eliminado con éxito!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchUserById() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID del usuario a buscar"));

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.obtenerUsuarioPorId(id);

            if (usuario != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("ID: ").append(usuario.getId())
                        .append(", Nombre: ").append(usuario.getNombre())
                        .append(", Saldo: ").append(usuario.getSaldo())
                        .append("\n");

                JOptionPane.showMessageDialog(null, sb.toString());
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
