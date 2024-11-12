package clases;

import java.util.Date;

public class MovimientoCuenta {
    private int id;
    private int cuentaId;
    private double monto;
    private String tipo;
    private String detalle;
    private Date fecha;

    public MovimientoCuenta(int id, int cuentaId, double monto, String tipo, String detalle, Date fecha) {
        this.id = id;
        this.cuentaId = cuentaId;
        this.monto = monto;
        this.tipo = tipo;
        this.detalle = detalle;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCuentaId() { return cuentaId; }
    public void setCuentaId(int cuentaId) { this.cuentaId = cuentaId; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}