public class Ex4_Cliente2 implements Runnable {
    private Ex4_Banco banco;

    public Ex4_Cliente2(Ex4_Banco b) {
        this.banco = b;
    }

    public void run() {
        for (int i = 0; i < 1000; ++i)
            this.banco.levantar(1, 5.0);
    }
}
