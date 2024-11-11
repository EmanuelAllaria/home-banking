package dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import clases.Transferencia;

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
        String sql = "INSERT INTO Transferencias (IdOrigen, IdDestino, Monto) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transferencia.getIdOrigen());
            pstmt.setInt(2, transferencia.getIdDestino());
            pstmt.setDouble(3, transferencia.getMonto());
            pstmt.executeUpdate();
        }
    }
}
