package Ex2;

public class Cliente2 implements Runnable {
    private Banco banco;

    public Cliente2(Banco b) { banco = b; }

    public void run() {
        for (int i = 0; i < 100000; ++i)
            this.banco.levantar(0, 5.0);
    }
}
