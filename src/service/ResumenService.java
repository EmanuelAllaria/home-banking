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
                sb.append("Cuenta ID: ").append(resumen.getCuentaId())
                        .append(", Ingresos: ").append(resumen.getIngresos())
                        .append(", Egresos: ").append(resumen.getEgresos())
                        .append(", Saldo Final: ").append(resumen.getSaldoFinal())
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
                    sb.append(", Ingresos: ").append(resumen.getIngresos())
                            .append(", Egresos: ").append(resumen.getEgresos())
                            .append(", Saldo Final: ").append(resumen.getSaldoFinal())
                            .append("\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateResumen() {
        try {
            int cuentaId = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la cuenta para generar el resumen"));
            List<Resumen> resumenes = resumenDAO.obtenerResumenPorCuentaId(cuentaId);
            StringBuilder sb = new StringBuilder();
            if (resumenes.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay resúmenes disponibles para la cuenta ID: " + cuentaId);
            } else {
                for (Resumen resumen : resumenes) {
                    sb.append(", Ingresos: ").append(resumen.getIngresos())
                            .append(", Egresos: ").append(resumen.getEgresos())
                            .append(", Saldo Final: ").append(resumen.getSaldoFinal())
                            .append("\n");
                }
                JOptionPane.showMessageDialog(null, sb.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}