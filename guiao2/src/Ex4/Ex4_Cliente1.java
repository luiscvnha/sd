public class Ex4_Cliente1 implements Runnable {
    private Ex4_Banco banco;

    public Ex4_Cliente1(Ex4_Banco b) {
        this.banco = b;
    }

    public void run() {
        for (int i = 0; i < 1000; ++i)
            this.banco.transferir(0, 1, 5.0);
    }
}
