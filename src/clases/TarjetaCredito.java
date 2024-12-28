package clases;

public class TarjetaCredito extends Tarjeta {
    private double tasaInteres;
    private double saldoAPagar;

    public TarjetaCredito(int id, int usuarioId, String numero, double limite, double saldo, double saldoAPagar, double tasaInteres) {
        super(id, usuarioId, numero, limite, saldo);
        this.saldoAPagar = saldoAPagar;
        this.tasaInteres = tasaInteres;
    }

    @Override
    public void realizarPago(double monto) {
        if (saldo + monto <= limite) {
            saldo += monto;
        } else {
            System.out.println("Límite excedido.");
        }
    }

    public double calcularIntereses() {
        return saldoAPagar * tasaInteres / 100;
    }

    // Métodos Getter y Setter
    public double getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(double tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public double getSaldoAPagar() {
        return saldoAPagar;
    }

    public void setSaldoAPagar(double saldoAPagar) {
        this.saldoAPagar = saldoAPagar;
    }
}
