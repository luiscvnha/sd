package Ex2;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Banco {
    private HashMap<Integer,Conta> contas;
    private ReentrantLock lockBanco;
    private int nextId = 0;

    public Banco(int n) {
        this.contas = new HashMap<>();
        this.lockBanco = new ReentrantLock();
        for (int i = 0; i < n; ++i)
            criarConta(0.0);
    }

    public int criarConta(double saldoInicial) {
        this.lockBanco.lock();

        int id = nextId++;
        Conta conta = new Conta(id, saldoInicial);
        this.contas.put(id, conta);

        this.lockBanco.unlock();

        return id;
    }

    public double fecharConta(int id) throws ContaInvalida {
        this.lockBanco.lock();

        if (!this.contas.containsKey(id)) {
            this.lockBanco.unlock();
            throw new ContaInvalida(id);
        }

        Conta conta = this.contas.get(id);
        conta.lock();

        double saldo = conta.consultar();
        this.contas.remove(id);

        this.lockBanco.unlock();
        conta.unlock();

        return saldo;
    }

    public double consultar(int id) throws ContaInvalida {
        this.lockBanco.lock();

        if (!this.contas.containsKey(id)) {
            this.lockBanco.unlock();
            throw new ContaInvalida(id);
        }

        Conta conta = this.contas.get(id);
        conta.lock();

        this.lockBanco.unlock();

        double r = conta.consultar();
        conta.unlock();

        return r;
    }

    public double consultarTotal(int[] id_contas) {
        Conta[] contas = new Conta[id_contas.length];
        int len = 0;

        this.lockBanco.lock();
        for (int id: id_contas)
            if (this.contas.containsKey(id)) {
                contas[len] = this.contas.get(id);
                contas[len].lock();
                ++len;
            }
        this.lockBanco.unlock();

        double r = 0.0;

        for (Conta c: contas) {
            r += c.consultar();
            c.unlock();
        }

        return r;
    }

    public void levantar(int id, double valor, String descricao) throws ContaInvalida, SaldoInsuficiente {
        this.lockBanco.lock();

        if (!this.contas.containsKey(id)) {
            // conta não existe
            this.lockBanco.unlock();
            throw new ContaInvalida(id);
        }

        // caso exista, adquirir lock
        Conta conta = this.contas.get(id);
        conta.lock();

        this.lockBanco.unlock();

        // consultar valor
        if (conta.consultar() < valor) {
            conta.unlock();
            throw new SaldoInsuficiente(id);
        }

        // levantar
        conta.levantar(valor, descricao);
        conta.unlock();
    }

    public void levantar(int id, double valor) throws ContaInvalida, SaldoInsuficiente {
        levantar(id, valor, "");
    }

    public void depositar(int id, double valor, String descricao) throws ContaInvalida {
        this.lockBanco.lock();

        if (!this.contas.containsKey(id)) {
            // conta não existe
            this.lockBanco.unlock();
            throw new ContaInvalida(id);
        }

        // caso exista, adquirir lock
        Conta conta = this.contas.get(id);
        conta.lock();

        this.lockBanco.unlock();

        // depositar valor
        conta.depositar(valor, descricao);

        conta.unlock();
    }

    public void depositar(int id, double valor) throws ContaInvalida {
        depositar(id, valor, "");
    }

    public void transferir(int id_origem, int id_destino, double valor, String descricao) throws ContaInvalida, SaldoInsuficiente {
        this.lockBanco.lock();

        if (!this.contas.containsKey(id_origem)) {
            this.lockBanco.unlock();
            throw new ContaInvalida(id_origem);
        } else if (!this.contas.containsKey(id_destino)) {
            this.lockBanco.unlock();
            throw new ContaInvalida(id_destino);
        }

        Conta c_origem = this.contas.get(id_origem);
        Conta c_destino = this.contas.get(id_destino);
        c_origem.lock();
        c_destino.lock();

        this.lockBanco.unlock();

        if (c_origem.consultar() < valor) {
            c_origem.unlock();
            c_destino.unlock();
            throw new SaldoInsuficiente(id_origem);
        }

        c_origem.levantar(valor, descricao);
        c_destino.depositar(valor, descricao);

        c_origem.unlock();
        c_destino.unlock();
    }

    public void transferir(int id_origem, int id_destino, double valor) throws ContaInvalida, SaldoInsuficiente {
        transferir(id_origem, id_destino, valor, "");
    }

    public List<Movimento> movimentos(int id) throws ContaInvalida {
        lockBanco.lock();

        if (!this.contas.containsKey(id)) {
            lockBanco.unlock();
            throw new ContaInvalida(id);
        }

        Conta conta = contas.get(id);

        conta.lock();
        lockBanco.unlock();

        List<Movimento> r = conta.getMovimentos();

        conta.unlock();

        return r;
    }
}
