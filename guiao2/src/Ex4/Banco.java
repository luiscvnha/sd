package Ex4;

public class Banco {
    private Conta[] contas;

    public Banco(int n) {
        this.contas = new Conta[n];
        for (int i = 0; i < n; ++i)
            contas[i] = new Conta();
    }

    public double consultar(int conta) {
        synchronized (this.contas[conta]) {
            return this.contas[conta].get();
        }
    }

    public void depositar(int conta, double valor) {
        synchronized (this.contas[conta]) {
            this.contas[conta].add(valor);
        }
    }

    public void levantar(int conta, double valor) {
        synchronized (this.contas[conta]) {
            this.contas[conta].sub(valor);
        }
    }

    public void transferir(int conta_src, int conta_dest, double valor) {
        int conta_menor_id = Math.min(conta_src, conta_dest),
            conta_maior_id = Math.max(conta_src, conta_dest);
        synchronized (this.contas[conta_menor_id]) {
            synchronized (this.contas[conta_maior_id]) {
                this.levantar(conta_src, valor);
                this.depositar(conta_dest, valor);
            }
        }
    }
}
