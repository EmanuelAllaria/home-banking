package clases;

import java.util.Date;

public class MovimientoCuenta extends Movimiento {

    private int cuentaId;
    private String tipo;
    private String detalle;


    public MovimientoCuenta(int id, int cuentaId, double monto, String tipo, String detalle, Date fecha) {
        super(id, monto, fecha);
        this.cuentaId = cuentaId;
        this.tipo = tipo;
        this.detalle = detalle;
    }

    public int getCuentaId() { return cuentaId; }
    public void setCuentaId(int cuentaId) { this.cuentaId = cuentaId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
   }