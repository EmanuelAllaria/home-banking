package dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import clases.Usuario;

public class UsuarioDAO {
    private static Connection connection;

    // Constructor predeterminado
    public UsuarioDAO() {
        // Obtiene la conexión única desde DatabaseConnection (Singleton)
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void crearUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuarios (Nombre, Saldo) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setDouble(2, usuario.getSaldo());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                usuario.setId(generatedKeys.getInt(1));
            }

            logAudit(usuario.getId(), "USUARIO CREADO", "Usuario creado con el balance: $" + usuario.getSaldo());
        }
    }

    public void actualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE Usuarios SET Nombre = ?, Saldo = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setDouble(2, usuario.getSaldo());
            pstmt.setInt(3, usuario.getId());
            pstmt.executeUpdate();
        }

        logAudit(usuario.getId(), "USUARIO ACTUALIZADO", "Usuario actualizado con el nuevo balance: $" + usuario.getSaldo());
    }

    public void eliminarUsuario(int id) throws SQLException {
        String sql = "DELETE FROM Usuarios WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }

        logAudit(id, "USUARIO ELIMINADO", "Usuario eliminado.");
    }

    public static Usuario obtenerUsuarioPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Usuarios WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("ID"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setSaldo(rs.getDouble("Saldo"));
                return usuario;
            }
        }
        return null;
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("ID"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setSaldo(rs.getDouble("Saldo"));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    public static Usuario obtenerUsuarioPorCuentaId(int cuentaId) throws SQLException {
        String sql = "SELECT Usuarios.* FROM Usuarios LEFT JOIN Cuentas ON Usuarios.ID = Cuentas.UsuarioID WHERE Cuentas.ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cuentaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("ID"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setSaldo(rs.getDouble("Saldo"));
                return usuario;
            }
        }
        return null;
    }

    public static void logAudit(int usuarioId, String action, String details) throws SQLException {
        String sql = "INSERT INTO AuditLogs (UsuarioID, Action, Details) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setString(2, action);
            pstmt.setString(3, details);
            pstmt.executeUpdate();
        }
    }
}