package clases;

import java.util.Date;

public class MovimientoTarjeta {
    private int id;
    private int tarjetaId;
    private double monto;
    private Date fecha;

    public MovimientoTarjeta(int id, int tarjetaId, double monto, Date fecha) {
        this.id = id;
        this.tarjetaId = tarjetaId;
        this.monto = monto;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTarjetaId() { return tarjetaId; }
    public void setTarjetaId(int tarjetaId) { this.tarjetaId = tarjetaId; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}
