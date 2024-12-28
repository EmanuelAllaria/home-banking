package clases;

public class TarjetaPrepaga extends Tarjeta {
    public TarjetaPrepaga(int id, int usuarioId, String numero, double limite, double saldo) {
        super(id, usuarioId, numero, limite, saldo);
    }

    @Override
    public void realizarPago(double monto) {
        if (saldo >= monto) {
            saldo -= monto;
        } else {
            System.out.println("Fondos insuficientes.");
        }
    }
}