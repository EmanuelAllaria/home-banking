package service;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import clases.*;
import dao.*;

public class CuentaService {

    private CuentaDAO cuentaDAO;

    public CuentaService() {
        // Inicializa CuentaDAO sin necesidad de pasar una conexión, pues esta se obtiene desde DatabaseConnection
        this.cuentaDAO = new CuentaDAO();
    }

    public List<Cuenta> listarCuentasPorUsuarioId(int usuarioId) throws SQLException {
        return cuentaDAO.listarCuentasPorUsuarioId(usuarioId);
    }

    public List<Cuenta> listarCuentasSinUsuarioId(int usuarioId) throws SQLException {
        return cuentaDAO.listarCuentasSinUsuarioId(usuarioId);
    }

    public void createAccount() {
        try {
            int usuarioId = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID del usuario"));
            String tipo = JOptionPane.showInputDialog("Ingrese tipo de cuenta (Ahorro/Corriente)");
            double saldo = Double.parseDouble(JOptionPane.showInputDialog("Ingrese saldo inicial"));

            Cuenta cuenta;
            if ("Ahorro".equalsIgnoreCase(tipo)) {
                cuenta = new CuentaAhorro(0, usuarioId, saldo);
                ((CuentaAhorro) cuenta).setTasaInteres(20.0);
            } else if ("Corriente".equalsIgnoreCase(tipo)) {
                cuenta = new CuentaCorriente(0, usuarioId, saldo, 1000.0);
            } else {
                JOptionPane.showMessageDialog(null, "Tipo de cuenta no válido");
                return;
            }

            cuentaDAO.crearCuenta(cuenta);
            JOptionPane.showMessageDialog(null, "Cuenta creada con éxito!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listAccounts(StringBuilder sb) {
        try {
            List<Cuenta> cuentas = cuentaDAO.listarCuentas();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            for (Cuenta cuenta : cuentas) {
                Usuario usuario = usuarioDAO.obtenerUsuarioPorId(cuenta.getUsuarioId());

                sb.append("Cuenta ID: ").append(cuenta.getId())
                        .append(", Tipo: ").append(cuenta instanceof CuentaCorriente ? "Corriente" : "Ahorro")
                        .append(", Saldo: ").append(cuenta.getSaldo());

                if (cuenta instanceof CuentaAhorro) {
                    sb.append(", Tasa de Interés: ").append(((CuentaAhorro) cuenta).getTasaInteres());
                } else if (cuenta instanceof CuentaCorriente) {
                    sb.append(", Límite de Sobregiro: ").append(((CuentaCorriente) cuenta).getLimiteSobregiro());
                }

                sb.append(", Usuario: ");
                if (usuario != null) {
                    sb.append(usuario.getNombre());
                } else {
                    sb.append("Usuario no encontrado");
                }

                sb.append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAccount() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la cuenta a actualizar"));
            String tipo = JOptionPane.showInputDialog("Ingrese nuevo tipo de cuenta (Ahorro/Corriente)");
            double saldo = Double.parseDouble(JOptionPane.showInputDialog("Ingrese nuevo saldo"));

            Cuenta cuenta;
            if ("Ahorro".equalsIgnoreCase(tipo)) {
                cuenta = new CuentaAhorro(id, 0, saldo);
                ((CuentaAhorro) cuenta).setTasaInteres(20.0);
            } else if ("Corriente".equalsIgnoreCase(tipo)) {
                cuenta = new CuentaCorriente(id, 0, saldo, 1000.0);
            } else {
                JOptionPane.showMessageDialog(null, "Tipo de cuenta no válido");
                return;
            }

            cuentaDAO.actualizarCuenta(cuenta);
            JOptionPane.showMessageDialog(null, "Cuenta actualizada con éxito!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la cuenta a eliminar"));

            cuentaDAO.eliminarCuenta(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> listarCuentasPorUsuarioId1(int usuarioId) {
        return null;
    }

    public void generarIntereses() {
        try {
            List<Cuenta> cuentas = cuentaDAO.listarCuentas();
            for (Cuenta cuenta : cuentas) {
                if (cuenta instanceof CuentaAhorro) {
                    double interes = cuenta.getSaldo() * (((CuentaAhorro) cuenta).getTasaInteres() / 100);
                    cuenta.setSaldo(cuenta.getSaldo() + interes);
                    cuentaDAO.actualizarCuenta(cuenta);
                    cuentaDAO.registrarMovimiento(cuenta.getId(), cuenta.getUsuarioId(), interes, "Cuenta");
                }
            }
            JOptionPane.showMessageDialog(null, "Intereses aplicados a todas las cuentas.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generarReporteMovimientos(int cuentaId, int mes, int anio) {
        try {
            List<MovimientoCuenta> movimientos = cuentaDAO.obtenerMovimientosPorMes(cuentaId, mes, anio);

            String fileName = "Reporte_Movimientos_Cuenta_" + cuentaId + "_" + mes + "_" + anio + ".txt";
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write("Reporte de Movimientos de la Cuenta ID: " + cuentaId + "\n");
                writer.write("Mes: " + mes + ", Año: " + anio + "\n");
                writer.write("===========================================\n");

                for (MovimientoCuenta movimiento : movimientos) {
                    writer.write("Fecha: " + movimiento.getFecha() + ", Monto: " + movimiento.getMonto() +  ", Tipo: " + movimiento.getTipo() +  ", Detalle: " + movimiento.getDetalle() + "\n");
                }

                writer.write("===========================================\n");
                JOptionPane.showMessageDialog(null, "Reporte generado: " + fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
