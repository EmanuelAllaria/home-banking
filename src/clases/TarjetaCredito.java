package clases;



public class TarjetaCredito extends Tarjeta {
    private double tasaInteres;
    private double saldoAPagar;

    public TarjetaCredito() {}

    public TarjetaCredito(int id, int usuarioId, String numero, double limite, double saldo, double saldoAPagar, double tasaInteres) {
        super(id, usuarioId, numero, limite, saldo);
        this.tasaInteres = tasaInteres;
        this.saldoAPagar = saldoAPagar;
    }

    public double getSaldoAPagar() {
        return saldoAPagar;
    }
    public void setSaldoAPagar(double saldoAPagar) {
        this.saldoAPagar = saldoAPagar;
    }
    public double getTasaInteres() {
        return tasaInteres; 
    }
    public void setTasaInteres(double tasaInteres) { 
        this.tasaInteres = tasaInteres; 
    }
}

