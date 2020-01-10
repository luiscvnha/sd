package Ex2;

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
}
