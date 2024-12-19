package service;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import clases.Resumen;
import dao.ResumenDAO;

public class ResumenService {

    private ResumenDAO resumenDAO;

    public ResumenService() {
        // Inicializa ResumenDAO, que obtiene la conexión desde DatabaseConnection
        this.resumenDAO = new ResumenDAO();
    }

    public void listResumenes(StringBuilder sb) {
        try {
            List<Resumen> resumenes = resumenDAO.listarResumenes();
            for (Resumen resumen : resumenes) {
                sb.append("Transferencia a cuenta ID: ").append(resumen.getIdDestino())
                        .append(", por: $").append(resumen.getMonto())
                        .append(", de la cuenta ID: ").append(resumen.getIdOrigen())
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getResumenByCuenta(int cuentaId, StringBuilder sb) {
        try {
            List<Resumen> resumenes = resumenDAO.obtenerResumenPorCuentaId(cuentaId);
            if (resumenes.isEmpty()) {
                sb.append("No hay resúmenes disponibles para la cuenta ID: ").append(cuentaId);
            } else {
                for (Resumen resumen : resumenes) {
                    sb.append("Transferencia a cuenta ID: ").append(resumen.getIdDestino())
                            .append(", por: $").append(resumen.getMonto())
                            .append(", de la cuenta ID: ").append(resumen.getIdOrigen())
                            .append("\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}