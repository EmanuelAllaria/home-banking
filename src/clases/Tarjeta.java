package clases;

public abstract class Tarjeta {
    private int id;
    private int cuentaId;
    private String numero;
    protected double saldo;
    protected double limite;

    public Tarjeta(int id, int cuentaId, String numero, double limite, double saldo) {
        this.id = id;
        this.cuentaId = cuentaId;
        this.numero = numero;
        this.limite = limite;
        this.saldo = saldo;
    }

    // Métodos Getter y Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCuentaId() { return cuentaId; }
    public void setCuentaId(int cuentaId) { this.cuentaId = cuentaId; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public double getLimite() { return limite; }
    public void setLimite(double limite) { this.limite = limite; }

    // Método para obtener el tipo de tarjeta
    public String getTipo() {
        return this.getClass().getSimpleName();
    }

    public abstract void realizarPago(double monto);

    @Override
    public String toString() {
        return "ID Tarjeta: " + id + ", UsuarioID: " + cuentaId + ", Número: " + numero +
                ", Saldo: " + saldo + ", Límite: " + limite;
    }
}
