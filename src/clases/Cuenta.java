package clases;

public abstract class Cuenta {
    private int id;
    private int usuarioId;
    protected double saldo;

    public Cuenta(int id, int usuarioId, double saldo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.saldo = saldo;
    }

    public void depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
        } else {
            System.out.println("Monto inválido para depósito.");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public abstract void retirar(double monto);

    @Override
    public String toString() {
        return "ID Cuenta: " + id + ", Saldo: " + saldo;
    }
}