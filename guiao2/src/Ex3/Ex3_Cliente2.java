public class Ex3_Cliente2 implements Runnable {
    private Ex3_Banco banco;

    public Ex3_Cliente2(Ex3_Banco b) {
        this.banco = b;
    }

    public void run() {
        for (int i = 0; i < 1000; ++i)
            this.banco.levantar(1, 5.0);
    }
}
