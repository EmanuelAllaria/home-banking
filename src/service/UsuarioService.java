package service;

import javax.swing.*;
import clases.Usuario;
import dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.List;

public class UsuarioService {

    public void createUser() {
        try {
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del usuario");
            double saldo = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el saldo inicial"));

            Usuario usuario = new Usuario(0, nombre, saldo);
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.crearUsuario(usuario);

            JOptionPane.showMessageDialog(null, "Usuario creado con éxito!\nID: " + usuario.getId());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<String> listUsers() {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<Usuario> usuarios = usuarioDAO.listarUsuarios();
            StringBuilder sb = new StringBuilder("Lista de Usuarios:\n");
            for (Usuario usuario : usuarios) {
                sb.append("ID: ").append(usuario.getId())
                        .append(", Nombre: ").append(usuario.getNombre())
                        .append(", Saldo: ").append(usuario.getSaldo())
                        .append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Usuarios", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public List<Usuario> listUsersForTransferencia() {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<Usuario> usuarios = usuarioDAO.listarUsuarios();
            return usuarios;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public void updateUser() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del usuario a actualizar"));
            String nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre");
            double saldo = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el nuevo saldo"));

            Usuario usuario = new Usuario(id, nombre, saldo);
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.actualizarUsuario(usuario);

            JOptionPane.showMessageDialog(null, "Usuario actualizado con éxito!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteUser() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del usuario a eliminar"));
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.eliminarUsuario(id);

            JOptionPane.showMessageDialog(null, "Usuario eliminado con éxito!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void searchUserById() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del usuario a buscar"));
            Usuario usuario = UsuarioDAO.obtenerUsuarioPorId(id);

            if (usuario != null) {
                JOptionPane.showMessageDialog(null, "ID: " + usuario.getId() +
                        "\nNombre: " + usuario.getNombre() +
                        "\nSaldo: " + usuario.getSaldo(), "Usuario Encontrado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Información", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
