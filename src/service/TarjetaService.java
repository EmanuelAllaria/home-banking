package service;


import javax.swing.*;

import clases.Tarjeta;
import clases.TarjetaCredito;
import clases.TarjetaDebito;
import clases.TarjetaPrepaga;
import clases.Usuario;
import dao.TarjetaDAO;
import dao.UsuarioDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class TarjetaService {

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
                    tarjeta = new TarjetaCredito(0, usuarioId, numero, limite, saldo, tasaInteres);
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
                    tarjeta = new TarjetaCredito(id, 0, numero, limite, saldo, tasaInteres);
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

            JOptionPane.showMessageDialog(null, sb.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

