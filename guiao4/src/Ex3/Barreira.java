package Ex3;

public class Barreira {
    private final int N;
    private int counter;
    private int etapa;

    public Barreira(int n) {
        N = n;
        counter = 0;
        etapa = 0;
    }

    public synchronized void esperar() {
        int minhaEtapa = etapa;
        ++counter;

        while (counter < N && minhaEtapa == etapa) {
            try { wait(); } catch (InterruptedException ignored) {}
        }

        if (counter == N) {
            ++etapa;
            counter = 0;
            notifyAll();
        }
    }
}
