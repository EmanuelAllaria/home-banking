package service;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import clases.Cuenta;
import clases.Usuario;
import dao.CuentaDAO;
import dao.UsuarioDAO;

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
            String tipo = JOptionPane.showInputDialog("Ingrese tipo de cuenta");
            double saldo = Double.parseDouble(JOptionPane.showInputDialog("Ingrese saldo inicial"));

            Cuenta cuenta = new Cuenta();
            cuenta.setUsuarioId(usuarioId);
            cuenta.setTipo(tipo);
            cuenta.setSaldo(saldo);

            // Usa la instancia existente de cuentaDAO
            cuentaDAO.crearCuenta(cuenta);

            JOptionPane.showMessageDialog(null, "Cuenta creada con éxito!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listAccounts(StringBuilder sb) {
        try {
            // Usa la instancia existente de cuentaDAO
            List<Cuenta> cuentas = cuentaDAO.listarCuentas();
            UsuarioDAO usuarioDAO = new UsuarioDAO();  // UsuarioDAO también obtiene la conexión desde DatabaseConnection

            for (Cuenta cuenta : cuentas) {
                Usuario usuario = usuarioDAO.obtenerUsuarioPorId(cuenta.getUsuarioId());
                sb.append("Cuenta ID: ").append(cuenta.getId())
                        .append(", Tipo: ").append(cuenta.getTipo())
                        .append(", Saldo: ").append(cuenta.getSaldo())
                        .append(", Usuario: ").append(usuario.getNombre())
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAccount() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la cuenta a actualizar"));
            String tipo = JOptionPane.showInputDialog("Ingrese nuevo tipo de cuenta");
            double saldo = Double.parseDouble(JOptionPane.showInputDialog("Ingrese nuevo saldo"));

            Cuenta cuenta = new Cuenta();
            cuenta.setId(id);
            cuenta.setTipo(tipo);
            cuenta.setSaldo(saldo);

            // Usa la instancia existente de cuentaDAO
            cuentaDAO.actualizarCuenta(cuenta);

            JOptionPane.showMessageDialog(null, "Cuenta actualizada con éxito!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la cuenta a eliminar"));

            // Usa la instancia existente de cuentaDAO
            cuentaDAO.eliminarCuenta(id);

            JOptionPane.showMessageDialog(null, "Cuenta eliminada con éxito!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> listarCuentasPorUsuarioId1(int usuarioId) {
        // TODO: Implementa este método si es necesario
        return null;
    }
}