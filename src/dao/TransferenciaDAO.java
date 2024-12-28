package dao;

import java.sql.*;
import clases.Transferencia;
import clases.Usuario;

public class TransferenciaDAO {
    private Connection connection;

    public TransferenciaDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void registrarTransferencia(Transferencia transferencia) throws SQLException {
        Usuario usuarioOrigen = UsuarioDAO.obtenerUsuarioPorCuentaId(transferencia.getIdOrigen());
        Usuario usuarioDestino = UsuarioDAO.obtenerUsuarioPorCuentaId(transferencia.getIdDestino());

        String sql = "INSERT INTO Transferencias (IdOrigen, IdDestino, Monto) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transferencia.getIdOrigen());
            pstmt.setInt(2, transferencia.getIdDestino());
            pstmt.setDouble(3, transferencia.getMonto());
            pstmt.executeUpdate();

            registrarMovimiento(transferencia.getIdOrigen(), transferencia.getIdDestino(), transferencia.getMonto());

            UsuarioDAO.logAudit(
                    usuarioOrigen.getId(),
                    "TRANSFERENCIA CREADA",
                    "Transferencia de $" + transferencia.getMonto() + " a cuenta ID: " + transferencia.getIdDestino()
            );

            UsuarioDAO.logAudit(
                    usuarioDestino.getId(),
                    "TRANSFERENCIA RECIBIDA",
                    "Transferencia de $" + transferencia.getMonto() + " desde cuenta ID: " + transferencia.getIdOrigen()
            );
        }
    }

    public void registrarMovimiento(int cuentaIdOrigen, int cuentaIdDestino, double monto) throws SQLException {
        String sql = "INSERT INTO MovimientosCuenta (CuentaID, Tipo, Monto, Detalle, Fecha) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Movimiento en la cuenta origen
            pstmt.setInt(1, cuentaIdOrigen);
            pstmt.setString(2, "Transferencia");
            pstmt.setDouble(3, -monto);
            pstmt.setString(4, "Transferencia enviada a cuenta ID: " + cuentaIdDestino);
            pstmt.setDate(5, new Date(System.currentTimeMillis()));
            pstmt.executeUpdate();

            // Movimiento en la cuenta destino
            pstmt.setInt(1, cuentaIdDestino);
            pstmt.setString(2, "Transferencia");
            pstmt.setDouble(3, monto);
            pstmt.setString(4, "Transferencia recibida de cuenta ID: " + cuentaIdOrigen);
            pstmt.setDate(5, new Date(System.currentTimeMillis()));
            pstmt.executeUpdate();
        }
    }
}
