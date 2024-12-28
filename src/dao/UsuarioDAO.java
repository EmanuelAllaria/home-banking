package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import clases.Usuario;

public class UsuarioDAO {
    private static Connection connection;

    public UsuarioDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        if (this.connection == null) {
            throw new IllegalStateException("Database connection is not initialized.");
        }
    }

    public void crearUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuarios (Nombre, Saldo) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setDouble(2, usuario.getSaldo());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                }
            }
            logAudit(usuario.getId(), "USUARIO CREADO", "Usuario creado con saldo: $" + usuario.getSaldo());
        }
    }

    public void actualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE Usuarios SET Nombre = ?, Saldo = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setDouble(2, usuario.getSaldo());
            pstmt.setInt(3, usuario.getId());
            pstmt.executeUpdate();
            logAudit(usuario.getId(), "USUARIO ACTUALIZADO", "Usuario actualizado con saldo: $" + usuario.getSaldo());
        }
    }

    public void eliminarUsuario(int id) throws SQLException {
        String cuentasQuery = "SELECT COUNT(*) FROM Cuentas WHERE UsuarioID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(cuentasQuery)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("El usuario tiene cuentas asociadas. Elimine las cuentas primero.");
                }
            }
        }

        String tarjetasQuery = "SELECT COUNT(*) FROM Tarjetas WHERE UsuarioID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(tarjetasQuery)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("El usuario tiene tarjetas asociadas. Elimine las tarjetas primero.");
                }
            }
        }

        String deleteQuery = "DELETE FROM Usuarios WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            logAudit(id, "USUARIO ELIMINADO", "Usuario con ID " + id + " eliminado.");
        }
    }

    public static Usuario obtenerUsuarioPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Usuarios WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("ID"),
                            rs.getString("Nombre"),
                            rs.getDouble("Saldo")
                    );
                }
            }
        }
        return null;
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("ID"),
                        rs.getString("Nombre"),
                        rs.getDouble("Saldo")
                ));
            }
        }
        return usuarios;
    }

    public static Usuario obtenerUsuarioPorCuentaId(int cuentaId) throws SQLException {
        String sql = """
            SELECT u.* 
            FROM Usuarios u 
            JOIN Cuentas c ON u.ID = c.UsuarioID 
            WHERE c.ID = ?
        """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cuentaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("ID"),
                            rs.getString("Nombre"),
                            rs.getDouble("Saldo")
                    );
                }
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
