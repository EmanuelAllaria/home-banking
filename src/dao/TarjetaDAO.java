package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import clases.Tarjeta;
import clases.TarjetaCredito;
import clases.TarjetaDebito;
import clases.TarjetaPrepaga;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarjetaDAO {
    private Connection connection;

    public TarjetaDAO() {
        // Obtiene la conexión única desde DatabaseConnection (Singleton)
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void crearTarjeta(Tarjeta tarjeta) throws SQLException {
        String sql = "INSERT INTO Tarjetas (UsuarioID, Numero, Limite, Saldo, Tipo, TasaInteres) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, tarjeta.getUsuarioId());
            pstmt.setString(2, tarjeta.getNumero());
            pstmt.setDouble(3, tarjeta.getLimite());
            pstmt.setDouble(4, tarjeta.getSaldo());
            pstmt.setString(5, tarjeta.getClass().getSimpleName());
            if (tarjeta instanceof TarjetaCredito) {
                pstmt.setDouble(6, ((TarjetaCredito) tarjeta).getTasaInteres());
            } else {
                pstmt.setNull(6, Types.DOUBLE);
            }
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                tarjeta.setId(generatedKeys.getInt(1));
            }
        }
    }

    public void actualizarTarjeta(Tarjeta tarjeta) throws SQLException {
        String sql = "UPDATE Tarjetas SET Numero = ?, Limite = ?, Saldo = ?, Tipo = ?, TasaInteres = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tarjeta.getNumero());
            pstmt.setDouble(2, tarjeta.getLimite());
            pstmt.setDouble(3, tarjeta.getSaldo());
            pstmt.setString(4, tarjeta.getClass().getSimpleName());
            if (tarjeta instanceof TarjetaCredito) {
                pstmt.setDouble(5, ((TarjetaCredito) tarjeta).getTasaInteres());
            } else {
                pstmt.setNull(5, Types.DOUBLE);
            }
            pstmt.setInt(6, tarjeta.getId());
            pstmt.executeUpdate();
        }
    }

    public void eliminarTarjeta(int id) throws SQLException {
        String sql = "DELETE FROM Tarjetas WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
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
                    tarjeta = new TarjetaCredito(rs.getInt("ID"), rs.getInt("UsuarioID"), rs.getString("Numero"), rs.getDouble("Limite"), rs.getDouble("Saldo"), rs.getDouble("TasaInteres"));
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
                    tarjeta = new TarjetaCredito(rs.getInt("ID"), rs.getInt("UsuarioID"), rs.getString("Numero"), rs.getDouble("Limite"), rs.getDouble("Saldo"), rs.getDouble("TasaInteres"));
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
}

