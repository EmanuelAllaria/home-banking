package clases;

import java.util.Date;

public class MovimientoTarjeta extends Movimiento {
    private int tarjetaId;

    public MovimientoTarjeta(int id, int tarjetaId, double monto, Date fecha) {
        super(id, monto, fecha);
        this.tarjetaId = tarjetaId;
    }

    public int getTarjetaId() { return tarjetaId; }
    public void setTarjetaId(int tarjetaId) { this.tarjetaId = tarjetaId; }
    }