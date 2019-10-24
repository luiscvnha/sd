public class Ex2_Cliente2 implements Runnable {
    private Ex2_Banco banco;

    public Ex2_Cliente2(Ex2_Banco b) {
        this.banco = b;
    }

    public void run() {
        for (int i = 0; i < 100000; ++i)
            this.banco.levantar(0, 5.0);
    }
}
