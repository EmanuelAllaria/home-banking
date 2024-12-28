package clases;

public class Transferencia {
    private int idOrigen;
    private int idDestino;
    private double monto;

    public Transferencia() {}

    public Transferencia(int idOrigen, int idDestino, double monto) {
        this.idOrigen = idOrigen;
        this.idDestino = idDestino;
        this.monto = monto;
    }

    public int getIdOrigen() { return idOrigen; }
    public void setIdOrigen(int idOrigen) { this.idOrigen = idOrigen; }
    public int getIdDestino() { return idDestino; }
    public void setIdDestino(int idDestino) { this.idDestino = idDestino; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
}
