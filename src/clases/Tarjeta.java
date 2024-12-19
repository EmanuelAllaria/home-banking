package clases;

public class Tarjeta {
    private int id;
    private int usuarioId;
    private String numero;
    private double limite;
    private double saldo;
    private String tipo;

    public Tarjeta() {}

    public Tarjeta(int id, int usuarioId, String numero, double limite, double saldo, String tipo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.numero = numero;
        this.limite = limite;
        this.saldo = saldo;
        this.tipo = tipo;
    }

    public Tarjeta(int id, int usuarioId, String numero, double limite, double saldo) {
        this(id, usuarioId, numero, limite, saldo, null);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public double getLimite() { return limite; }
    public void setLimite(double limite) { this.limite = limite; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
