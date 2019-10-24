public class Ex2_Banco {
    private double[] contas;

    public Ex2_Banco(int n) {
        this.contas = new double[n];
        for (int i = 0; i < n; ++i)
            this.contas[i] = 0.0;
    }

    public double consultar(int conta) {
        return this.contas[conta];
    }

    public synchronized void depositar(int conta, double valor) {
        this.contas[conta] += valor;
    }

    public synchronized void levantar(int conta, double valor) {
        this.contas[conta] -= valor;
    }
}
