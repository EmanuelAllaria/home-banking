package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import clases.Resumen;
import clases.Usuario;

public class ResumenDAO {
    private Connection connection;

    public ResumenDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        if (this.connection == null) {
            throw new IllegalStateException("Database connection is not initialized.");
        }
    }

    public List<Resumen> obtenerResumenPorCuentaId(int cuentaId) throws SQLException {
        List<Resumen> resumenes = new ArrayList<>();
        String sql = """
        SELECT transferencias.*, cuentas.ID AS CuentaID, cuentas.UsuarioID
        FROM transferencias
        LEFT JOIN cuentas ON cuentas.ID = transferencias.IdOrigen
        WHERE cuentas.ID = ?;
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cuentaId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Resumen resumen = crearResumenDesdeResultSet(rs);
                    resumenes.add(resumen);
                }
            }
        }

        Usuario usuario = UsuarioDAO.obtenerUsuarioPorCuentaId(cuentaId);
        if (usuario != null) {
            UsuarioDAO.logAudit(usuario.getId(), "OBTENER RESUMEN CUENTA",
                    "Resumen obtenido por: " + usuario.getNombre() + ", con ID : " + usuario.getId());
        }

        return resumenes;
    }

    public List<Resumen> listarResumenes() throws SQLException {
        List<Resumen> resumenes = new ArrayList<>();
        String sql = """
        SELECT transferencias.*, cuentas.ID AS CuentaID, cuentas.UsuarioID
        FROM transferencias
        LEFT JOIN cuentas ON cuentas.ID = transferencias.IdOrigen;
        """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Resumen resumen = crearResumenDesdeResultSet(rs);
                resumenes.add(resumen);
            }
        }
        return resumenes;
    }

    private Resumen crearResumenDesdeResultSet(ResultSet rs) throws SQLException {
        return new Resumen(
                rs.getInt("ID"),
                rs.getInt("UsuarioID"),
                rs.getDouble("Monto"),
                rs.getInt("IdOrigen"),
                rs.getInt("IdDestino")
        );
    }
}
