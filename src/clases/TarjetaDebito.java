package clases;

import javax.swing.*;

public class TarjetaDebito extends Tarjeta {
    public TarjetaDebito(int id, int cuentaId, String numero, double limite, double saldo) {
        super(id, cuentaId, numero, limite, saldo);
    }

    @Override
    public void realizarPago(double monto) {
        if (saldo >= monto) {
            saldo -= monto;
        } else {
            JOptionPane.showMessageDialog(null, "Fondos insuficientes.");
            System.out.println("Fondos insuficientes.");
        }
    }
}