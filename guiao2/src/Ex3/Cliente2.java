package Ex3;

public class Cliente2 implements Runnable {
    private Banco banco;

    public Cliente2(Banco b) { banco = b; }

    public void run() {
        for (int i = 0; i < 1000; ++i)
            this.banco.levantar(1, 5.0);
    }
}
