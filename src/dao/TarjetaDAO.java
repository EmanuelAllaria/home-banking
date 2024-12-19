package dao;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import clases.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarjetaDAO {
    private static Connection connection;

    public TarjetaDAO() {
        // Obtiene la conexión única desde DatabaseConnection (Singleton)
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void actualizarSaldo(int tarjetaId, double nuevoSaldo) throws SQLException {
        Tarjeta tarjeta = obtenerTarjetaPorId(tarjetaId);
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorId(tarjeta.getUsuarioId());

        String sql = "UPDATE Tarjetas SET Saldo = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevoSaldo);
            pstmt.setInt(2, tarjetaId);
            pstmt.executeUpdate();

            UsuarioDAO.logAudit(usuario.getId(), "SALDO ACTUALIZADO", "Saldo actualizado por: " + usuario.getNombre() + ", con ID : " + usuario.getId() + ", monto de $" + nuevoSaldo);
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el saldo: " + e.getMessage(), e);
        }
    }

    public void actualizarSaldoAPagar(int tarjetaId, double saldo) throws SQLException {
        Tarjeta tarjeta = obtenerTarjetaPorId(tarjetaId);
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorId(tarjeta.getUsuarioId());

        assert tarjeta instanceof TarjetaCredito;
        double saldoAPagarAnterior = ((TarjetaCredito) tarjeta).getSaldoAPagar();
        double tasaInteres = ((TarjetaCredito) tarjeta).getTasaInteres();
        double saldoAPagar = saldoAPagarAnterior + (saldo + ((saldo * tasaInteres) / 100));

        String sql = "UPDATE Tarjetas SET SaldoAPagar = ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, saldoAPagar);
            stmt.setInt(2, tarjetaId);
            int filasActualizadas = stmt.executeUpdate();

            if (filasActualizadas == 0) {
                throw new SQLException("No se pudo actualizar el SaldoAPagar para la tarjeta con ID: " + tarjetaId);
            }

            UsuarioDAO.logAudit(usuario.getId(), "SALDO A PAGAR ACTUALIZADO", "Saldo a pagar actualizado por: " + usuario.getNombre() + ", con ID : " + usuario.getId() + ", monto de $" + saldoAPagar);
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el saldo: " + e.getMessage(), e);
        }
    }

    public static Tarjeta obtenerTarjetaPorId(Integer tarjetaID) throws SQLException {
        String sql = "SELECT * FROM Tarjetas WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tarjetaID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("Tipo");
                    if ("TarjetaCredito".equals(tipo)) {
                        TarjetaCredito tarjetaCredito = new TarjetaCredito();
                        tarjetaCredito.setId(rs.getInt("ID"));
                        tarjetaCredito.setUsuarioId(rs.getInt("UsuarioID"));
                        tarjetaCredito.setNumero(rs.getString("Numero"));
                        tarjetaCredito.setLimite(rs.getDouble("Limite"));
                        tarjetaCredito.setSaldo(rs.getDouble("Saldo"));
                        tarjetaCredito.setSaldoAPagar(rs.getDouble("SaldoAPagar"));
                        tarjetaCredito.setTipo(tipo);
                        tarjetaCredito.setTasaInteres(rs.getDouble("TasaInteres"));
                        return tarjetaCredito;
                    } else {
                        Tarjeta tarjeta = new Tarjeta();
                        tarjeta.setId(rs.getInt("ID"));
                        tarjeta.setUsuarioId(rs.getInt("UsuarioID"));
                        tarjeta.setNumero(rs.getString("Numero"));
                        tarjeta.setLimite(rs.getDouble("Limite"));
                        tarjeta.setSaldo(rs.getDouble("Saldo"));
                        tarjeta.setTipo(tipo);
                        return tarjeta;
                    }
                }
            }
        }
        return null;
    }

    public void crearTarjeta(Tarjeta tarjeta) throws SQLException {
        String sql = "INSERT INTO Tarjetas (UsuarioID, Numero, Limite, Saldo, Tipo, TasaInteres, SaldoAPagar) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, tarjeta.getUsuarioId());
            pstmt.setString(2, tarjeta.getNumero());
            pstmt.setDouble(3, tarjeta.getLimite());
            pstmt.setDouble(4, tarjeta.getSaldo());
            pstmt.setString(5, tarjeta.getClass().getSimpleName());
            if (tarjeta instanceof TarjetaCredito) {
                pstmt.setDouble(6, ((TarjetaCredito) tarjeta).getTasaInteres());
                pstmt.setDouble(7, ((TarjetaCredito) tarjeta).getSaldoAPagar());
            } else {
                pstmt.setNull(6, Types.DOUBLE);
                pstmt.setNull(7, Types.DOUBLE);
            }
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                tarjeta.setId(generatedKeys.getInt(1));
            }

            UsuarioDAO.logAudit(tarjeta.getUsuarioId(), "TARJETA DE " + tarjeta.getClass().getSimpleName().toUpperCase() + " CREADA", "Tarjeta de " + tarjeta.getClass().getSimpleName() + " creada con el balance: $" + tarjeta.getSaldo() + ", con el limite de: $" + tarjeta.getLimite());
        }
    }

    public void actualizarTarjeta(Tarjeta tarjeta) throws SQLException {
        String sql = "UPDATE Tarjetas SET Numero = ?, Limite = ?, Saldo = ?, Tipo = ?, TasaInteres = ?, SaldoAPagar = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tarjeta.getNumero());
            pstmt.setDouble(2, tarjeta.getLimite());
            pstmt.setDouble(3, tarjeta.getSaldo());
            pstmt.setString(4, tarjeta.getClass().getSimpleName());
            if (tarjeta instanceof TarjetaCredito) {
                pstmt.setDouble(5, ((TarjetaCredito) tarjeta).getTasaInteres());
                pstmt.setDouble(6, ((TarjetaCredito) tarjeta).getSaldoAPagar());
            } else {
                pstmt.setNull(5, Types.DOUBLE);
                pstmt.setNull(6, Types.DOUBLE);
            }
            pstmt.setInt(7, tarjeta.getId());
            pstmt.executeUpdate();

            UsuarioDAO.logAudit(tarjeta.getUsuarioId(), "TARJETA DE " + tarjeta.getClass().getSimpleName().toUpperCase() + " ACTUALIZADA", "Tarjeta de " + tarjeta.getClass().getSimpleName() + " actualizada con el nuevo balance: $" + tarjeta.getSaldo() + ", con el nuevo limite de: $" + tarjeta.getLimite());
        }
    }

    public void eliminarTarjeta(int id) throws SQLException {
        Tarjeta tarjeta = obtenerTarjetaPorId(id);
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorId(tarjeta.getUsuarioId());

        String sql = "DELETE FROM Tarjetas WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            UsuarioDAO.logAudit(usuario.getId(), "TARJETA ELIMINADA", "Tarjeta eliminada.");
        }
    }

    public List<Tarjeta> listarTarjetas() throws SQLException {
        List<Tarjeta> tarjetas = new ArrayList<>();
        String sql = "SELECT * FROM Tarjetas";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Tarjeta tarjeta;
                String tipo = rs.getString("Tipo");
                if (tipo.equals("TarjetaCredito")) {
                    tarjeta = new TarjetaCredito(rs.getInt("ID"), rs.getInt("UsuarioID"), rs.getString("Numero"), rs.getDouble("Limite"), rs.getDouble("Saldo"), rs.getDouble("SaldoAPagar"), rs.getDouble("TasaInteres"));
                } else if (tipo.equals("TarjetaDebito")) {
                    tarjeta = new TarjetaDebito(rs.getInt("ID"), rs.getInt("UsuarioID"), rs.getString("Numero"), rs.getDouble("Limite"), rs.getDouble("Saldo"));
                } else {
                    tarjeta = new TarjetaPrepaga(rs.getInt("ID"), rs.getInt("UsuarioID"), rs.getString("Numero"), rs.getDouble("Limite"), rs.getDouble("Saldo"));
                }
                tarjetas.add(tarjeta);
            }
        }
        return tarjetas;
    }

    public List<Tarjeta> listarTarjetasPorUsuarioId(int usuarioId) throws SQLException {
        List<Tarjeta> tarjetas = new ArrayList<>();
        String sql = "SELECT * FROM Tarjetas WHERE UsuarioID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Tarjeta tarjeta;
                String tipo = rs.getString("Tipo");
                if (tipo.equals("TarjetaCredito")) {
                    tarjeta = new TarjetaCredito(rs.getInt("ID"), rs.getInt("UsuarioID"), rs.getString("Numero"), rs.getDouble("Limite"), rs.getDouble("Saldo"), rs.getDouble("SaldoAPagar"), rs.getDouble("TasaInteres"));
                } else if (tipo.equals("TarjetaDebito")) {
                    tarjeta = new TarjetaDebito(rs.getInt("ID"), rs.getInt("UsuarioID"), rs.getString("Numero"), rs.getDouble("Limite"), rs.getDouble("Saldo"));
                } else {
                    tarjeta = new TarjetaPrepaga(rs.getInt("ID"), rs.getInt("UsuarioID"), rs.getString("Numero"), rs.getDouble("Limite"), rs.getDouble("Saldo"));
                }
                tarjetas.add(tarjeta);
            }
        }
        return tarjetas;
    }

    public void registrarMovimiento(int tarjetaId, double monto) throws SQLException {
        Tarjeta tarjeta = obtenerTarjetaPorId(tarjetaId);
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorId(tarjeta.getUsuarioId());

        String sql = "INSERT INTO MovimientosTarjeta (TarjetaID, Monto, Fecha) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tarjetaId);
            pstmt.setDouble(2, monto);
            pstmt.setDate(3, new Date(System.currentTimeMillis()));
            pstmt.executeUpdate();

            UsuarioDAO.logAudit(usuario.getId(), "MOVIMIENTO DE TARJETA REGISTRADA", "Movimiento de $" + monto + ", con la tarjeta con ID: " + tarjetaId);
        }
    }

    public List<MovimientoTarjeta> obtenerMovimientosPorMes(int tarjetaId, int mes, int anio) throws SQLException {
        Tarjeta tarjeta = obtenerTarjetaPorId(tarjetaId);
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorId(tarjeta.getUsuarioId());

        List<MovimientoTarjeta> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM MovimientosTarjeta WHERE TarjetaID = ? AND MONTH(Fecha) = ? AND YEAR(Fecha) = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tarjetaId);
            pstmt.setInt(2, mes);
            pstmt.setInt(3, anio);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MovimientoTarjeta movimiento = new MovimientoTarjeta(
                        rs.getInt("ID"),
                        rs.getInt("TarjetaID"),
                        rs.getDouble("Monto"),
                        rs.getDate("Fecha")
                );
                movimientos.add(movimiento);
            }
        }

        UsuarioDAO.logAudit(usuario.getId(), "MOVIMIENTO DE TARJETA OBTENIDA", "Movimiento de la tarjeta con el ID: " + tarjetaId + " obtenida, para el mes " + mes + " de " + anio);
        return movimientos;
    }
}

