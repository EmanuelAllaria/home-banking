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
    }

    public List<Resumen> obtenerResumenPorCuentaId(int cuentaId) throws SQLException {
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorCuentaId(cuentaId);

        List<Resumen> resumenes = new ArrayList<>();
        String sql = """
            SELECT 
                c.ID AS cuentaId,
                SUM(CASE WHEN t.IdDestino = ? THEN t.Monto ELSE 0 END) AS ingresos,
                SUM(CASE WHEN t.IdOrigen = ? THEN t.Monto ELSE 0 END) AS egresos,
                c.Saldo AS saldoFinal
            FROM 
                Cuentas c
            LEFT JOIN 
                Transferencias t ON c.ID = t.IdOrigen OR c.ID = t.IdDestino
            WHERE 
                c.ID = ?
            GROUP BY 
                c.ID, c.Saldo
            ORDER BY 
                c.ID DESC;
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cuentaId);
            pstmt.setInt(2, cuentaId);
            pstmt.setInt(3, cuentaId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Resumen resumen = new Resumen();
                resumen.setCuentaId(rs.getInt("cuentaId"));
                resumen.setIngresos(rs.getDouble("ingresos"));
                resumen.setEgresos(rs.getDouble("egresos"));
                resumen.setSaldoFinal(rs.getDouble("saldoFinal"));
                resumenes.add(resumen);
            }
        }

        UsuarioDAO.logAudit(usuario.getId(), "OBTENER RESUMEN CUENTA", "Resumen obtenido por: " + usuario.getNombre() + ", con ID : " + usuario.getId());
        return resumenes;
    }

    public List<Resumen> listarResumenes() throws SQLException {
        List<Resumen> resumenes = new ArrayList<>();
        String sql = """
        SELECT 
            c.ID AS cuentaId,
            SUM(CASE WHEN t.IdDestino = c.ID THEN t.Monto ELSE 0 END) AS ingresos,
            SUM(CASE WHEN t.IdOrigen = c.ID THEN t.Monto ELSE 0 END) AS egresos,
            c.Saldo AS saldoFinal
        FROM 
            Cuentas c
        LEFT JOIN 
            Transferencias t ON c.ID = t.IdOrigen OR c.ID = t.IdDestino
        GROUP BY 
            c.ID, c.Saldo
        ORDER BY 
            c.ID DESC;
    """;

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Resumen resumen = new Resumen();
                resumen.setCuentaId(rs.getInt("cuentaId"));
                resumen.setIngresos(rs.getDouble("ingresos"));
                resumen.setEgresos(rs.getDouble("egresos"));
                resumen.setSaldoFinal(rs.getDouble("saldoFinal"));
                resumenes.add(resumen);
            }
        }
        return resumenes;
    }
}