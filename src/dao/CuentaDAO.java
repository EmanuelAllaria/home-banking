package dao;




import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import clases.Cuenta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {
    private Connection connection;

    public CuentaDAO() {
        // Obtiene la conexión única desde DatabaseConnection (Singleton)
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void crearCuenta(Cuenta cuenta) throws SQLException {
        String sql = "INSERT INTO Cuentas (UsuarioID, Tipo, Saldo) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, cuenta.getUsuarioId());
            pstmt.setString(2, cuenta.getTipo());
            pstmt.setDouble(3, cuenta.getSaldo());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                cuenta.setId(generatedKeys.getInt(1));
            }
        }
    }

    public void actualizarCuenta(Cuenta cuenta) throws SQLException {
        String sql = "UPDATE Cuentas SET Tipo = ?, Saldo = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cuenta.getTipo());
            pstmt.setDouble(2, cuenta.getSaldo());
            pstmt.setInt(3, cuenta.getId());
            pstmt.executeUpdate();
        }
    }

    public void eliminarCuenta(int id) throws SQLException {
        String sql = "DELETE FROM Cuentas WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Cuenta obtenerCuentaPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Cuentas WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setId(rs.getInt("ID"));
                cuenta.setUsuarioId(rs.getInt("UsuarioID"));
                cuenta.setTipo(rs.getString("Tipo"));
                cuenta.setSaldo(rs.getDouble("Saldo"));
                return cuenta;
            }
        }
        return null;
    }

    public List<Cuenta> listarCuentas() throws SQLException {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT * FROM Cuentas";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setId(rs.getInt("ID"));
                cuenta.setUsuarioId(rs.getInt("UsuarioID"));
                cuenta.setTipo(rs.getString("Tipo"));
                cuenta.setSaldo(rs.getDouble("Saldo"));
                cuentas.add(cuenta);
            }
        }
        return cuentas;
    }

    public List<Cuenta> listarCuentasPorUsuarioId(int usuarioId) throws SQLException {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT * FROM Cuentas WHERE UsuarioID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setId(rs.getInt("ID"));
                cuenta.setUsuarioId(rs.getInt("UsuarioID"));
                cuenta.setTipo(rs.getString("Tipo"));
                cuenta.setSaldo(rs.getDouble("Saldo"));
                cuentas.add(cuenta);
            }
        }
        return cuentas;
    }

    public List<Cuenta> listarCuentasSinUsuarioId(int usuarioId) throws SQLException {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT * FROM Cuentas WHERE UsuarioID != ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setId(rs.getInt("ID"));
                cuenta.setUsuarioId(rs.getInt("UsuarioID"));
                cuenta.setTipo(rs.getString("Tipo"));
                cuenta.setSaldo(rs.getDouble("Saldo"));
                cuentas.add(cuenta);
            }
        }
        return cuentas;
    }
}



