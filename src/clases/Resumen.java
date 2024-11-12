package clases;

public class Resumen {
    private int id;
    private int usuarioId;
    private double monto;
    private int idOrigen;
    private int idDestino;

    public Resumen() {}

    public Resumen(int id, int usuarioId, double monto, int idOrigen, int idDestino) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.monto = monto;
        this.idOrigen = idOrigen;
        this.idDestino = idDestino;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public int getIdOrigen() { return idOrigen; }
    public void setIdOrigen(int idOrigen) { this.idOrigen = idOrigen; }
    public int getIdDestino() { return idDestino; }
    public void setIdDestino(int idDestino) { this.idDestino = idDestino; }
}