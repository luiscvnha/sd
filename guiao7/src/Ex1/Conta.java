package Ex1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Conta {
    private int id;
    private double saldo;
    private Lock lockConta = new ReentrantLock();

    public Conta(int id) {
        this.id = id;
        this.saldo = 0.0;
    }

    public Conta(int id, double saldo) {
        this.id = id;
        this.saldo = saldo;
    }

    public double consultar() { return saldo; }

    public void depositar(double valor) { saldo += valor; }

    public void levantar(double valor) { saldo -= valor; }

    public void lock() { lockConta.lock(); }

    public void unlock() { lockConta.unlock(); }
}
