package service;


import javax.swing.*;

import clases.*;
import dao.CuentaDAO;
import dao.DatabaseConnection;
import dao.TarjetaDAO;
import dao.UsuarioDAO;

import java.sql.*;
import java.util.List;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class TarjetaService {
    private Connection connection;

    public TarjetaService() {
        // Obtiene la conexión única desde DatabaseConnection (Singleton)
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void createCard() {
        try {
            int usuarioId = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID del usuario"));
            String numero = JOptionPane.showInputDialog("Ingrese número de tarjeta");
            double limite = Double.parseDouble(JOptionPane.showInputDialog("Ingrese límite"));
            double saldo = Double.parseDouble(JOptionPane.showInputDialog("Ingrese saldo"));
            String tipoTarjeta = JOptionPane.showInputDialog("Ingrese tipo de tarjeta (Credito/Debito/Prepaga)");

            Tarjeta tarjeta;
            switch (tipoTarjeta) {
                case "Credito":
                    double tasaInteres = Double.parseDouble(JOptionPane.showInputDialog("Ingrese tasa de interés"));
                    tarjeta = new TarjetaCredito(0, usuarioId, numero, limite, saldo, 0.0, tasaInteres);
                    break;
                case "Debito":
                    tarjeta = new TarjetaDebito(0, usuarioId, numero, limite, saldo);
                    break;
                case "Prepaga":
                    tarjeta = new TarjetaPrepaga(0, usuarioId, numero, limite, saldo);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de tarjeta no válido");
            }

            TarjetaDAO tarjetaDAO = new TarjetaDAO();
            tarjetaDAO.crearTarjeta(tarjeta);

            JOptionPane.showMessageDialog(null, "Tarjeta creada con éxito!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCard() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la tarjeta a actualizar"));
            String numero = JOptionPane.showInputDialog("Ingrese nuevo número de tarjeta");
            double limite = Double.parseDouble(JOptionPane.showInputDialog("Ingrese nuevo límite"));
            double saldo = Double.parseDouble(JOptionPane.showInputDialog("Ingrese nuevo saldo"));
            String tipoTarjeta = JOptionPane.showInputDialog("Ingrese tipo de tarjeta (Credito/Debito/Prepaga)");

            Tarjeta tarjeta;
            switch (tipoTarjeta) {
                case "Credito":
                    double tasaInteres = Double.parseDouble(JOptionPane.showInputDialog("Ingrese tasa de interés"));
                    double saldoAPagar = Double.parseDouble(JOptionPane.showInputDialog("Ingrese nuevo saldo a pagar"));
                    tarjeta = new TarjetaCredito(id, 0, numero, limite, saldo, saldoAPagar, tasaInteres);
                    break;
                case "Debito":
                    tarjeta = new TarjetaDebito(id, 0, numero, limite, saldo);
                    break;
                case "Prepaga":
                    tarjeta = new TarjetaPrepaga(id, 0, numero, limite, saldo);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de tarjeta no válido");
            }

            TarjetaDAO tarjetaDAO = new TarjetaDAO();
            tarjetaDAO.actualizarTarjeta(tarjeta);

            JOptionPane.showMessageDialog(null, "Tarjeta actualizada con éxito!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCard() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la tarjeta a eliminar"));

            TarjetaDAO tarjetaDAO = new TarjetaDAO();
            tarjetaDAO.eliminarTarjeta(id);

            JOptionPane.showMessageDialog(null, "Tarjeta eliminada con éxito!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listCards() {
        try {
            TarjetaDAO tarjetaDAO = new TarjetaDAO();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            List<Tarjeta> tarjetas = tarjetaDAO.listarTarjetas();
            StringBuilder sb = new StringBuilder();

            for (Tarjeta tarjeta : tarjetas) {
                Usuario usuario = usuarioDAO.obtenerUsuarioPorId(tarjeta.getUsuarioId());
                sb.append("Tarjeta ID: ").append(tarjeta.getId())
                        .append(", Número: ").append(tarjeta.getNumero())
                        .append(", Límite: ").append(tarjeta.getLimite())
                        .append(", Saldo: ").append(tarjeta.getSaldo())
                        .append(", Usuario: ").append(usuario.getNombre())
                        .append("\n");
            }

            JOptionPane.showMessageDialog(null, sb.toString(),"Lista de tarjetas", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registrarDebito() {
        try {
            int tarjetaId = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la tarjeta"));
            double monto = Double.parseDouble(JOptionPane.showInputDialog("Ingrese monto del débito"));

            TarjetaDAO tarjetaDAO = new TarjetaDAO();
            Tarjeta tarjeta = tarjetaDAO.obtenerTarjetaPorId(tarjetaId);

            if (tarjeta != null) {
                double saldoActual = tarjeta.getSaldo();
                double nuevoSaldo = saldoActual - monto;
                tarjetaDAO.actualizarSaldo(tarjetaId, nuevoSaldo);
                if (Objects.equals(tarjeta.getTipo(), "TarjetaCredito")) {
                    tarjetaDAO.actualizarSaldoAPagar(tarjetaId, monto);
                }
                tarjetaDAO.registrarMovimiento(tarjetaId, monto);
                registrarMovimiento(tarjetaId, monto);
                JOptionPane.showMessageDialog(null, "Débito registrado con éxito","Debitos", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Tarjeta no encontrada");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registrarMovimiento(int tarjetaId, double monto) throws SQLException {
        CuentaDAO cuentaDAO = new CuentaDAO();
        List<Cuenta> cuentas = cuentaDAO.listarCuentasPorTarjetaId(tarjetaId);
        Cuenta cuenta;
        if (!cuentas.isEmpty()) {
            cuenta = cuentas.get(0);
        } else {
            throw new SQLException("No se encontró ninguna cuenta asociada a la tarjeta.");
        }

        String sql = "INSERT INTO MovimientosCuenta (CuentaID, Tipo, Monto, Detalle, Fecha) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cuenta.getId());
            pstmt.setString(2, "Tarjeta");
            pstmt.setDouble(3, monto);
            pstmt.setString(4, "Debito registrado por: $" + monto);
            pstmt.setDate(5, new Date(System.currentTimeMillis()));
            pstmt.executeUpdate();
        }
    }

    public void emitirResumenMensual() {
        try {
            int tarjetaId = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la tarjeta"));
            int mes = Integer.parseInt(JOptionPane.showInputDialog("Ingrese mes (1-12)"));
            int anio = Integer.parseInt(JOptionPane.showInputDialog("Ingrese año"));
            TarjetaDAO tarjetaDAO = new TarjetaDAO();

            List<MovimientoTarjeta> movimientos = tarjetaDAO.obtenerMovimientosPorMes(tarjetaId, mes, anio);
            StringBuilder sb = new StringBuilder();
            sb.append("Resumen de movimientos de la tarjeta ID ").append(tarjetaId)
                    .append(" para el mes ").append(mes).append("/").append(anio).append(":\n");

            for (MovimientoTarjeta mov : movimientos) {
                sb.append("Fecha: ").append(mov.getFecha()).append(", Monto: ").append(mov.getMonto()).append("\n");
            }

            JOptionPane.showMessageDialog(null, sb.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

