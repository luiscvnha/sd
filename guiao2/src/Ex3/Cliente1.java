package Ex3;

public class Cliente1 implements Runnable {
    private Banco banco;

    public Cliente1(Banco b) { banco = b; }

    public void run() {
        for (int i = 0; i < 1000; ++i)
            this.banco.transferir(0, 1, 5.0);
    }
}
