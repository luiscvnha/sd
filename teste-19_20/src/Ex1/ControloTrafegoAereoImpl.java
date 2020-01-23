package Ex1;


public class ControloTrafegoAereoImpl implements ControloTrafegoAereo {
    private final int NUM;
    private final int MAX;
    private boolean[] pistas;
    private int descolagensSeguidas;
    private int aterragensSeguidas;
    private int descolagens;
    private int proxIdDescolagem;
    private int aterragens;
    private int proxIdAterragem;


    public ControloTrafegoAereoImpl(int num, int max) {
        NUM = num;
        MAX = max;
        pistas = new boolean[NUM];
        for (int i = 0; i < NUM; ++i)
            pistas[i] = false;
        descolagensSeguidas = aterragensSeguidas = 0;
        descolagens = aterragens = 0;
        proxIdDescolagem = proxIdAterragem = 0;
    }

    public synchronized int pedirParaDescolar() {
        int id = proxIdDescolagem++;
        int pista;

        while (!(id == descolagens
                && (pista = pistaLivre()) >= 0
                && (proxIdAterragem <= aterragens || descolagensSeguidas < MAX))) {
            try { wait(); } catch (InterruptedException ignored) {}
        }

        pistas[pista] = true;
        ++descolagens;
        ++descolagensSeguidas;
        if (descolagensSeguidas == MAX)
            aterragensSeguidas = 0;

        return pista;
    }

    public synchronized int pedirParaAterrar() {
        int id = proxIdAterragem++;
        int pista;

        while (!(id == aterragens
                && (pista = pistaLivre()) >= 0
                && (proxIdDescolagem <= descolagens || aterragensSeguidas < MAX))) {
            try { wait(); } catch (InterruptedException ignored) {}
        }

        pistas[pista] = true;
        ++aterragens;
        ++aterragensSeguidas;
        if (aterragensSeguidas == MAX)
            descolagensSeguidas = 0;

        return pista;
    }

    public synchronized void descolou(int pista) {
        pistas[pista] = false;
        notifyAll();
    }

    public synchronized void aterrou(int pista) {
        pistas[pista] = false;
        notifyAll();
    }

    private int pistaLivre() {
        for (int i = 0; i < NUM; ++i) {
            if (!pistas[i])
                return i;
        }

        return -1;
    }
}
