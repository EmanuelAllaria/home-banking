package clases;

public class CuentaAhorro extends Cuenta {
    private double tasaInteres;

    public CuentaAhorro(int id, int usuarioId, double saldo) {
        super(id, usuarioId, saldo);
    }

    public double getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(double tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    @Override
    public void retirar(double monto) {
        if (saldo >= monto) {
            saldo -= monto;
        } else {
            System.out.println("Fondos insuficientes.");
        }
    }
}