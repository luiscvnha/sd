public class Ex2_Cliente1 implements Runnable {
    private Ex2_Banco banco;

    public Ex2_Cliente1(Ex2_Banco b) {
        this.banco = b;
    }

    public void run() {
        for (int i = 0; i < 100000; ++i)
            this.banco.depositar(0, 5.0);
    }
}
