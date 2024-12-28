package service;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import clases.Resumen;
import dao.ResumenDAO;

public class ResumenService {

    private ResumenDAO resumenDAO;

    public ResumenService() {
        this.resumenDAO = new ResumenDAO();
    }

    public void listarResumenes(StringBuilder sb) {
        try {
            List<Resumen> resumenes = resumenDAO.listarResumenes();
            if (resumenes.isEmpty()) {
                sb.append("No hay resúmenes disponibles.\n");
            } else {
                for (Resumen resumen : resumenes) {
                    sb.append(formatearResumen(resumen));
                }
            }
        } catch (SQLException e) {
            manejarExcepcion(e);
        }
    }

    public void obtenerResumenPorCuenta(int cuentaId, StringBuilder sb) {
        try {
            List<Resumen> resumenes = resumenDAO.obtenerResumenPorCuentaId(cuentaId);
            if (resumenes.isEmpty()) {
                sb.append("No hay resúmenes disponibles para la cuenta ID: ").append(cuentaId).append("\n");
            } else {
                for (Resumen resumen : resumenes) {
                    sb.append(formatearResumen(resumen));
                }
            }
        } catch (SQLException e) {
            manejarExcepcion(e);
        }
    }

    private String formatearResumen(Resumen resumen) {
        return "Transferencia a cuenta ID: " + resumen.getIdDestino() +
                ", por: $" + resumen.getMonto() +
                ", de la cuenta ID: " + resumen.getIdOrigen() + "\n";
    }

    private void manejarExcepcion(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Ocurrió un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
