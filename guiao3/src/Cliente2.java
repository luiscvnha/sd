public class Cliente2 implements Runnable {
    private Banco banco;

    public Cliente2(Banco b) {
        this.banco = b;
    }

    public void run() {
        for (int i = 0; i < 1000; ++i)
            try {
                this.banco.levantar(1, 5.0);
            } catch (SaldoInsuficiente | ContaInvalida e) {
                e.printStackTrace();
            }
    }
}
