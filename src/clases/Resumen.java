package clases;

public class Resumen {
    private int id;
    private int cuentaId;
    private double ingresos;
    private double egresos;
    private double saldoFinal;

    public Resumen() {}

    public Resumen(int id, int cuentaId, double ingresos, double egresos, double saldoFinal) {
        this.id = id;
        this.cuentaId = cuentaId;
        this.ingresos = ingresos;
        this.egresos = egresos;
        this.saldoFinal = saldoFinal;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCuentaId() { return cuentaId; }
    public void setCuentaId(int cuentaId) { this.cuentaId = cuentaId; }
    public double getIngresos() { return ingresos; }
    public void setIngresos(double ingresos) { this.ingresos = ingresos; }
    public double getEgresos() { return egresos; }
    public void setEgresos(double egresos) { this.egresos = egresos; }
    public double getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(double saldoFinal) { this.saldoFinal = saldoFinal; }
}