import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Banco {
    private Map<Integer, Conta> contas;
    private ReentrantLock lockBanco;
    private int nextId = 0;

    public Banco(int n) {
        contas = new HashMap<>();
        lockBanco = new ReentrantLock();
        for (int i = 0; i < n; ++i)
            criarConta(0.0);
    }

    public int criarConta(double saldoInicial) {
        lockBanco.lock();

        int id = nextId++;
        Conta conta = new Conta(id, saldoInicial);
        contas.put(id, conta);

        lockBanco.unlock();

        return id;
    }

    public double fecharConta(int id) throws ContaInvalida {
        lockBanco.lock();

        if (!contas.containsKey(id)) {
            lockBanco.unlock();
            throw new ContaInvalida(id);
        }

        Conta conta = contas.get(id);
        conta.lock();

        double saldo = conta.consultar();
        contas.remove(id);

        lockBanco.unlock();
        conta.unlock();

        return saldo;
    }

    public double consultar(int id) throws ContaInvalida {
        lockBanco.lock();

        if (!contas.containsKey(id)) {
            lockBanco.unlock();
            throw new ContaInvalida(id);
        }

        Conta conta = contas.get(id);
        conta.lock();

        lockBanco.unlock();

        double r = conta.consultar();
        conta.unlock();

        return r;
    }

    public double consultarTotal(int[] id_contas) {
        Conta[] contas = new Conta[id_contas.length];
        int len = 0;

        lockBanco.lock();
        for (int id: id_contas)
            if (this.contas.containsKey(id)) {
                contas[len] = this.contas.get(id);
                contas[len].lock();
                ++len;
            }
        lockBanco.unlock();

        double r = 0.0;

        for (Conta c: contas) {
            r += c.consultar();
            c.unlock();
        }

        return r;
    }

    public void levantar(int id, double valor) throws ContaInvalida, SaldoInsuficiente {
        lockBanco.lock();

        if (!contas.containsKey(id)) {
            // conta não existe
            lockBanco.unlock();
            throw new ContaInvalida(id);
        }

        // caso exista, adquirir lock
        Conta conta = contas.get(id);
        conta.lock();

        lockBanco.unlock();

        // consultar valor
        if (conta.consultar() < valor) {
            conta.unlock();
            throw new SaldoInsuficiente(id);
        }

        // levantar
        conta.levantar(valor);
        conta.unlock();
    }

    public void depositar(int id, double valor) throws ContaInvalida {
        lockBanco.lock();

        if (!contas.containsKey(id)) {
            // conta não existe
            lockBanco.unlock();
            throw new ContaInvalida(id);
        }

        // caso exista, adquirir lock
        Conta conta = contas.get(id);
        conta.lock();

        lockBanco.unlock();

        // depositar valor
        conta.depositar(valor);

        conta.unlock();
    }

    public void transferir(int id_origem, int id_destino, double valor) throws ContaInvalida, SaldoInsuficiente {
        lockBanco.lock();

        if (!contas.containsKey(id_origem)) {
            lockBanco.unlock();
            throw new ContaInvalida(id_origem);
        } else if (!contas.containsKey(id_destino)) {
            lockBanco.unlock();
            throw new ContaInvalida(id_destino);
        }

        Conta c_origem = contas.get(id_origem);
        c_origem.lock();
        Conta c_destino = contas.get(id_destino);
        c_destino.lock();

        lockBanco.unlock();

        if (c_origem.consultar() < valor) {
            c_origem.unlock();
            c_destino.unlock();
            throw new SaldoInsuficiente(id_origem);
        }

        c_origem.levantar(valor);
        c_origem.unlock();
        c_destino.depositar(valor);
        c_destino.unlock();
    }
}
