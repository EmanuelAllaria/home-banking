package clases;

public abstract class Tarjeta {
    private int id;
    private int usuarioId;
    private String numero;
    protected double saldo;
    protected double limite;

    public Tarjeta(int id, int usuarioId, String numero, double limite, double saldo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.numero = numero;
        this.limite = limite;
        this.saldo = saldo;
    }

    // Métodos Getter y Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
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
        return "ID Tarjeta: " + id + ", UsuarioID: " + usuarioId + ", Número: " + numero +
                ", Saldo: " + saldo + ", Límite: " + limite;
    }
}
