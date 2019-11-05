package Ex3;

public class Banco {
    private double[] contas;

    public Banco(int n) {
        this.contas = new double[n];
        for (int i = 0; i < n; ++i)
            this.contas[i] = 0.0;
    }

    public synchronized double consultar(int conta) {
        return this.contas[conta];
    }

    public synchronized void depositar(int conta, double valor) {
        this.contas[conta] += valor;
    }

    public synchronized void levantar(int conta, double valor) {
        this.contas[conta] -= valor;
    }

    public synchronized void transferir(int conta_src, int conta_dest, double valor) {
        this.levantar(conta_src, valor);
        this.depositar(conta_dest, valor);
    }
}
