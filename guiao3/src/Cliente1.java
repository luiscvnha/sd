public class Cliente1 implements Runnable {
    private Banco banco;

    public Cliente1(Banco b) {
        this.banco = b;
    }

    public void run() {
        for (int i = 0; i < 1000; ++i)
            try {
                this.banco.transferir(0, 1, 5.0);
            } catch (SaldoInsuficiente | ContaInvalida e) {
                e.printStackTrace();
            }
    }
}
