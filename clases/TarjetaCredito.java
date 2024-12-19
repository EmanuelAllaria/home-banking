package clases;



public class TarjetaCredito extends Tarjeta {
    private double tasaInteres;

    public TarjetaCredito(int id, int usuarioId, String numero, double limite, double saldo, double tasaInteres) {
        super(id, usuarioId, numero, limite, saldo);
        this.tasaInteres = tasaInteres;
    }

    public double getTasaInteres() { 
        return tasaInteres; 
    }
    public void setTasaInteres(double tasaInteres) { 
        this.tasaInteres = tasaInteres; 
    }
}

