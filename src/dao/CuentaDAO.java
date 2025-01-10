package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import clases.*;

import javax.swing.*;

public class CuentaDAO {
    private Connection connection;

    public CuentaDAO() {
        // Obtiene la conexión única desde DatabaseConnection (Singleton)
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void crearCuenta(Cuenta cuenta) throws SQLException {
        String sql = "INSERT INTO Cuentas (UsuarioID, Tipo, Saldo, TasaInteres) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, cuenta.getUsuarioId());
            pstmt.setString(2, cuenta instanceof CuentaCorriente ? "Corriente" : "Ahorro");
            pstmt.setDouble(3, cuenta.getSaldo());
            pstmt.setDouble(4, cuenta instanceof CuentaAhorro ? ((CuentaAhorro) cuenta).getTasaInteres() : 0);
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                cuenta.setId(generatedKeys.getInt(1));
            }

            UsuarioDAO.logAudit(cuenta.getUsuarioId(), "CUENTA CREADA", "Cuenta creada con el balance: $" + cuenta.getSaldo());
        }
    }

    public void actualizarCuenta(Cuenta cuenta) throws SQLException {
        String sql = "UPDATE Cuentas SET Tipo = ?, Saldo = ?, TasaInteres = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cuenta instanceof CuentaCorriente ? "Corriente" : "Ahorro");
            pstmt.setDouble(2, cuenta.getSaldo());
            pstmt.setDouble(3, cuenta instanceof CuentaAhorro ? ((CuentaAhorro) cuenta).getTasaInteres() : 0);
            pstmt.setInt(4, cuenta.getId());
            pstmt.executeUpdate();

            UsuarioDAO.logAudit(cuenta.getUsuarioId(), "CUENTA ACTUALIZADA", "Cuenta actualizada con el nuevo balance: $" + cuenta.getSaldo());
        }
    }

    public void eliminarCuenta(int id) throws SQLException {
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorCuentaId(id);
        TarjetaDAO tarjetaDAO = new TarjetaDAO();
        Tarjeta tarjeta = tarjetaDAO.obtenerTarjetaPorCuentaId(id);
        if (tarjeta != null) {
            JOptionPane.showMessageDialog(null, "Error al borrar la cuenta, tiene tarjetas asociadas", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String sql = "DELETE FROM Cuentas WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            UsuarioDAO.logAudit(usuario.getId(), "CUENTA ELIMINADA", "Cuenta eliminada.");
            JOptionPane.showMessageDialog(null, "Cuenta eliminada con éxito!");
        }
    }

    public Cuenta obtenerCuentaPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Cuentas WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String tipo = rs.getString("Tipo");
                Cuenta cuenta;
                if ("Corriente".equalsIgnoreCase(tipo)) {
                    cuenta = new CuentaCorriente(
                            rs.getInt("ID"),
                            rs.getInt("UsuarioID"),
                            rs.getDouble("Saldo"),
                            0 // Puedes agregar lógica para límite de sobregiro
                    );
                } else {
                    cuenta = new CuentaAhorro(
                            rs.getInt("ID"),
                            rs.getInt("UsuarioID"),
                            rs.getDouble("Saldo")
                    );
                    ((CuentaAhorro) cuenta).setTasaInteres(rs.getDouble("TasaInteres"));
                }
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
                String tipo = rs.getString("Tipo");
                Cuenta cuenta;
                if ("Corriente".equalsIgnoreCase(tipo)) {
                    cuenta = new CuentaCorriente(
                            rs.getInt("ID"),
                            rs.getInt("UsuarioID"),
                            rs.getDouble("Saldo"),
                            0 // Puedes agregar lógica para límite de sobregiro
                    );
                } else {
                    cuenta = new CuentaAhorro(
                            rs.getInt("ID"),
                            rs.getInt("UsuarioID"),
                            rs.getDouble("Saldo")
                    );
                    ((CuentaAhorro) cuenta).setTasaInteres(rs.getDouble("TasaInteres"));
                }
                cuentas.add(cuenta);
            }
        }
        return cuentas;
    }

    public List<MovimientoCuenta> obtenerMovimientosPorMes(int cuentaId, int mes, int anio) throws SQLException {
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorCuentaId(cuentaId);

        List<MovimientoCuenta> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM MovimientosCuenta WHERE CuentaID = ? AND MONTH(Fecha) = ? AND YEAR(Fecha) = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cuentaId);
            pstmt.setInt(2, mes);
            pstmt.setInt(3, anio);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MovimientoCuenta movimiento = new MovimientoCuenta(
                        rs.getInt("ID"),
                        rs.getInt("CuentaID"),
                        rs.getDouble("Monto"),
                        rs.getString("Tipo"),
                        rs.getString("Detalle"),
                        rs.getDate("Fecha")
                );
                movimientos.add(movimiento);
            }
        }

        UsuarioDAO.logAudit(usuario.getId(), "OBTENER RESUMEN CUENTA", "Resumen creado por: " + usuario.getNombre() + ", con ID : " + usuario.getId());
        return movimientos;
    }

    public void registrarMovimiento(int cuentaId, int usuarioId, double monto, String tipo) throws SQLException {
        Usuario usuarioOrigen = UsuarioDAO.obtenerUsuarioPorCuentaId(cuentaId);
        Usuario usuarioDAO = UsuarioDAO.obtenerUsuarioPorId(usuarioId);
        String detalle = null;
        if ("Transferencia".equals(tipo)) {
            detalle = "Transferencia a: " + usuarioDAO.getNombre();
        } else if ("Tarjeta".equals(tipo)) {
            detalle = "Debito registrado por: $: " + monto;
        } else if ("Cuenta".equals(tipo)) {
            detalle = "Intereses generados: $: " + monto;
        }

        String sql = "INSERT INTO MovimientosCuenta (CuentaID, Tipo, Monto, Detalle, Fecha) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cuentaId);
            pstmt.setString(2, tipo);
            pstmt.setDouble(3, monto);
            pstmt.setString(4, detalle);
            pstmt.setDate(5, new Date(System.currentTimeMillis()));
            pstmt.executeUpdate();

            UsuarioDAO.logAudit(usuarioOrigen.getId(), tipo, detalle);
        }
    }

    public List<Cuenta> listarCuentasPorUsuarioId(int usuarioId) throws SQLException {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT * FROM Cuentas WHERE UsuarioID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String tipo = rs.getString("Tipo");
                Cuenta cuenta;
                if ("Corriente".equalsIgnoreCase(tipo)) {
                    cuenta = new CuentaCorriente(
                            rs.getInt("ID"),
                            rs.getInt("UsuarioID"),
                            rs.getDouble("Saldo"),
                            0 // Puedes agregar lógica para límite de sobregiro
                    );
                } else {
                    cuenta = new CuentaAhorro(
                            rs.getInt("ID"),
                            rs.getInt("UsuarioID"),
                            rs.getDouble("Saldo")
                    );
                    ((CuentaAhorro) cuenta).setTasaInteres(rs.getDouble("TasaInteres"));
                }
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
                String tipo = rs.getString("Tipo");
                Cuenta cuenta;
                if ("Corriente".equalsIgnoreCase(tipo)) {
                    cuenta = new CuentaCorriente(
                            rs.getInt("ID"),
                            rs.getInt("UsuarioID"),
                            rs.getDouble("Saldo"),
                            0 // Puedes agregar lógica para límite de sobregiro
                    );
                } else {
                    cuenta = new CuentaAhorro(
                            rs.getInt("ID"),
                            rs.getInt("UsuarioID"),
                            rs.getDouble("Saldo")
                    );
                    ((CuentaAhorro) cuenta).setTasaInteres(rs.getDouble("TasaInteres"));
                }
                cuentas.add(cuenta);
            }
        }
        return cuentas;
    }

    public List<Cuenta> listarCuentasPorTarjetaId(int tarjetaId) throws SQLException {
        String sql = """
                    SELECT c.* FROM Cuentas c
                    JOIN Tarjetas t ON c.ID = t.CuentaID
                    WHERE t.ID = ?
                """;
        List<Cuenta> cuentas = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tarjetaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("Tipo");
                    Cuenta cuenta;
                    if ("Corriente".equalsIgnoreCase(tipo)) {
                        cuenta = new CuentaCorriente(
                                rs.getInt("ID"),
                                rs.getInt("UsuarioID"),
                                rs.getDouble("Saldo"),
                                0 // Ajustar lógica de sobregiro si es necesario
                        );
                    } else {
                        cuenta = new CuentaAhorro(
                                rs.getInt("ID"),
                                rs.getInt("UsuarioID"),
                                rs.getDouble("Saldo")
                        );
                        ((CuentaAhorro) cuenta).setTasaInteres(rs.getDouble("TasaInteres"));
                    }
                    cuentas.add(cuenta);
                }
            }
        }
        return cuentas;
    }
}