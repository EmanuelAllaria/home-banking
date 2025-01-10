package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import clases.*;

public class TarjetaDAO {
    private static Connection connection;

    public TarjetaDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void actualizarSaldo(int tarjetaId, double nuevoSaldo) throws SQLException {
        Tarjeta tarjeta = obtenerTarjetaPorId(tarjetaId);
        if (tarjeta == null) throw new SQLException("Tarjeta no encontrada.");

        String sql = "UPDATE Tarjetas SET Saldo = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevoSaldo);
            pstmt.setInt(2, tarjetaId);
            pstmt.executeUpdate();
            logMovimientoTarjeta(tarjeta, "SALDO ACTUALIZADO", nuevoSaldo);
        }
    }

    public void actualizarSaldoAPagar(int tarjetaId, double monto) throws SQLException {
        Tarjeta tarjeta = obtenerTarjetaPorId(tarjetaId);
        if (!(tarjeta instanceof TarjetaCredito)) throw new SQLException("Solo aplica a tarjetas de crédito.");

        TarjetaCredito tarjetaCredito = (TarjetaCredito) tarjeta;
        double nuevoSaldoAPagar = monto * (1 + tarjetaCredito.getTasaInteres() / 100);
        String sql = "UPDATE Tarjetas SET SaldoAPagar = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevoSaldoAPagar);
            pstmt.setInt(2, tarjetaId);
            pstmt.executeUpdate();
            logMovimientoTarjeta(tarjeta, "SALDO A PAGAR ACTUALIZADO", nuevoSaldoAPagar);
        }
    }

    public Tarjeta obtenerTarjetaPorId(int tarjetaId) throws SQLException {
        String sql = "SELECT * FROM Tarjetas WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tarjetaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return crearTarjetaDesdeResultSet(rs);
            }
        }
        return null;
    }

    public Tarjeta obtenerTarjetaPorCuentaId(int cuentaId) throws SQLException {
        String sql = "SELECT * FROM Tarjetas WHERE CuentaId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cuentaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return crearTarjetaDesdeResultSet(rs);
            }
        }
        return null;
    }

    public void crearTarjeta(Tarjeta tarjeta) throws SQLException {
        String sql = """
            INSERT INTO Tarjetas (CuentaId, Numero, Limite, Saldo, Tipo, TasaInteres, SaldoAPagar)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepararParametrosTarjeta(pstmt, tarjeta);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) tarjeta.setId(rs.getInt(1));
            }

            logMovimientoTarjeta(tarjeta, "TARJETA CREADA", tarjeta.getSaldo());
        }
    }

    public void actualizarTarjeta(Tarjeta tarjeta) throws SQLException {
        String sql = """
            UPDATE Tarjetas SET Numero = ?, Limite = ?, Saldo = ?, Tipo = ?, TasaInteres = ?, SaldoAPagar = ?
            WHERE ID = ?
        """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            prepararParametrosTarjeta(pstmt, tarjeta);
            pstmt.setInt(7, tarjeta.getId());
            pstmt.executeUpdate();
            logMovimientoTarjeta(tarjeta, "TARJETA ACTUALIZADA", tarjeta.getSaldo());
        }
    }

    public void eliminarTarjeta(int tarjetaId) throws SQLException {
        String sql = "DELETE FROM Tarjetas WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tarjetaId);
            pstmt.executeUpdate();
            System.out.println("Tarjeta con ID " + tarjetaId + " eliminada.");
        }
    }

    public List<Tarjeta> listarTarjetas() throws SQLException {
        List<Tarjeta> tarjetas = new ArrayList<>();
        String sql = "SELECT * FROM Tarjetas";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) tarjetas.add(crearTarjetaDesdeResultSet(rs));
        }
        return tarjetas;
    }

    private Tarjeta crearTarjetaDesdeResultSet(ResultSet rs) throws SQLException {
        String tipo = rs.getString("Tipo");
        if ("TarjetaCredito".equals(tipo)) {
            return new TarjetaCredito(
                    rs.getInt("ID"), rs.getInt("CuentaId"),
                    rs.getString("Numero"), rs.getDouble("Limite"),
                    rs.getDouble("Saldo"), rs.getDouble("SaldoAPagar"),
                    rs.getDouble("TasaInteres")
            );
        } else if ("TarjetaDebito".equals(tipo)) {
            return new TarjetaDebito(
                    rs.getInt("ID"), rs.getInt("CuentaId"),
                    rs.getString("Numero"), rs.getDouble("Limite"),
                    rs.getDouble("Saldo")
            );
        } else if ("TarjetaPrepaga".equals(tipo)) {
            return new TarjetaPrepaga(
                    rs.getInt("ID"), rs.getInt("CuentaId"),
                    rs.getString("Numero"), rs.getDouble("Limite"),
                    rs.getDouble("Saldo")
            );
        }
        throw new SQLException("Tipo de tarjeta no reconocido.");
    }

    private void prepararParametrosTarjeta(PreparedStatement pstmt, Tarjeta tarjeta) throws SQLException {
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
    }

    private void logMovimientoTarjeta(Tarjeta tarjeta, String accion, double monto) {
        System.out.println(accion + ": Tarjeta " + tarjeta.getId() + ", Monto: $" + monto);
    }

    public void registrarMovimiento(int tarjetaId, double monto) throws SQLException {
        Tarjeta tarjeta = obtenerTarjetaPorId(tarjetaId);
        if (tarjeta == null) throw new SQLException("Tarjeta no encontrada.");

        String sql = "INSERT INTO MovimientosTarjeta (TarjetaID, Monto, Fecha) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tarjetaId);
            pstmt.setDouble(2, monto);
            pstmt.setDate(3, new Date(System.currentTimeMillis()));
            pstmt.executeUpdate();
            logMovimientoTarjeta(tarjeta, "MOVIMIENTO REGISTRADO", monto);
        }
    }

    public List<MovimientoTarjeta> obtenerMovimientosPorMes(int tarjetaId, int mes, int anio) throws SQLException {
        String sql = """
            SELECT * FROM MovimientosTarjeta
            WHERE TarjetaID = ? AND MONTH(Fecha) = ? AND YEAR(Fecha) = ?
        """;
        List<MovimientoTarjeta> movimientos = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tarjetaId);
            pstmt.setInt(2, mes);
            pstmt.setInt(3, anio);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movimientos.add(new MovimientoTarjeta(
                            rs.getInt("ID"),
                            rs.getInt("TarjetaID"),
                            rs.getDouble("Monto"),
                            rs.getDate("Fecha")
                    ));
                }
            }
        }
        return movimientos;
    }
}
