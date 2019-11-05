import java.util.concurrent.locks.ReentrantLock;

public class Conta {
    private int id;
    private double saldo;
    private ReentrantLock lockConta;

    public Conta(int id) {
        this.id = id;
        this.saldo = 0.0;
        this.lockConta = new ReentrantLock();
    }

    public Conta(int id, double saldo) {
        this.id = id;
        this.saldo = saldo;
        this.lockConta = new ReentrantLock();
    }

    public void lock() { this.lockConta.lock(); }

    public void unlock() { this.lockConta.unlock(); }

    public double consultar() { return saldo; }

    public void depositar(double valor) { saldo += valor; }

    public void levantar(double valor) { saldo -= valor; }
}
