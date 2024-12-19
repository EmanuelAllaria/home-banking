package dao;



import java.sql.*;

import clases.Transferencia;
import clases.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransferenciaDAO {
    private Connection connection;

    public TransferenciaDAO() {
        // Obtiene la conexión única desde DatabaseConnection (Singleton)
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void registrarTransferencia(Transferencia transferencia) throws SQLException {
        Usuario usuarioDAO = UsuarioDAO.obtenerUsuarioPorCuentaId(transferencia.getIdOrigen());

        String sql = "INSERT INTO Transferencias (IdOrigen, IdDestino, Monto) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transferencia.getIdOrigen());
            pstmt.setInt(2, transferencia.getIdDestino());
            pstmt.setDouble(3, transferencia.getMonto());
            pstmt.executeUpdate();
            registrarMovimiento(transferencia.getIdOrigen(), transferencia.getIdDestino(), transferencia.getMonto());

            UsuarioDAO.logAudit(usuarioDAO.getId(), "TRANSFERENCIA CREADA", "Transferencia creada con el monto: $" + transferencia.getMonto() + " a la cuenta con el ID: " + transferencia.getIdDestino() + " desde la cuenta con el ID: " + transferencia.getIdOrigen());
        }
    }

    public void registrarMovimiento(int CuentaID, int CuentaIDOrigen, double monto) throws SQLException {
        Usuario usuarioDAO = UsuarioDAO.obtenerUsuarioPorCuentaId(CuentaIDOrigen);
        String nombreUsuario = usuarioDAO.getNombre();

        String sql = "INSERT INTO MovimientosCuenta (CuentaID, Tipo, Monto, Detalle, Fecha) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, CuentaID);
            pstmt.setString(2, "Transferencia");
            pstmt.setDouble(3, monto);
            pstmt.setString(4, "Transferencia a: " + nombreUsuario);
            pstmt.setDate(5, new Date(System.currentTimeMillis()));
            pstmt.executeUpdate();

            UsuarioDAO.logAudit(usuarioDAO.getId(), "RESUMEN CUENTA CREADA", "Resumen creado por: " + usuarioDAO.getNombre() + ", con ID : " + usuarioDAO.getId());
        }
    }
}
