package Ex2;

import static Ex2.Movimento.Operacao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Conta {
    private int id;
    private double saldo;
    private Lock lockConta = new ReentrantLock();
    private List<Movimento> movimentos = new ArrayList<>();

    public Conta(int id) {
        this.id = id;
        this.saldo = 0.0;
    }

    public Conta(int id, double saldo) {
        this.id = id;
        this.saldo = saldo;
    }

    public double consultar() { return saldo; }

    public void depositar(double valor, String descricao) {
        saldo += valor;
        movimentos.add(new Movimento(Operacao.DEPOSITO, descricao, valor, saldo));
    }

    public void levantar(double valor, String descricao) {
        saldo -= valor;
        movimentos.add(new Movimento(Operacao.LEVANTAMENTO, descricao, valor, saldo));
    }

    public List<Movimento> getMovimentos() {
        List<Movimento> r = new ArrayList<>();
        for (Movimento m : movimentos)
            r.add(m.clone());
        return r;
    }

    public void lock() { lockConta.lock(); }

    public void unlock() { lockConta.unlock(); }
}
