package clases;



public class Cuenta {
    private int id;
    private int usuarioId;
    private String tipo;
    private double saldo;

    public Cuenta() {}

    public Cuenta(int id, int usuarioId, String tipo, double saldo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "ID: " + this.id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
}
