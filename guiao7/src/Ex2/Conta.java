package Ex2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Conta {
    private int id;
    private double saldo;
    private ReentrantLock lockConta;
    private List<Movimento> movimentos;

    public Conta(int id) {
        this.id = id;
        this.saldo = 0.0;
        this.lockConta = new ReentrantLock();
        this.movimentos = new ArrayList<>();
    }

    public Conta(int id, double saldo) {
        this.id = id;
        this.saldo = saldo;
        this.lockConta = new ReentrantLock();
        this.movimentos = new ArrayList<>();
    }

    public void lock() { this.lockConta.lock(); }

    public void unlock() { this.lockConta.unlock(); }

    public double consultar() { return saldo; }

    public void depositar(double valor, String descricao) {
        saldo += valor;
        movimentos.add(new Movimento(ID_Operacao.DEPOSITO, descricao, valor, saldo));
    }

    public void levantar(double valor, String descricao) {
        saldo -= valor;
        movimentos.add(new Movimento(ID_Operacao.LEVANTAMENTO, descricao, valor, saldo));
    }

    public List<Movimento> getMovimentos() {
        List<Movimento> r = new ArrayList<>();
        for (Movimento m : movimentos)
            r.add(m.clone());
        return r;
    }
}
