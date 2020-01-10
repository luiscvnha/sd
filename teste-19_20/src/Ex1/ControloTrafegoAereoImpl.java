package Ex1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;


public class ControloTrafegoAereoImpl implements ControloTrafegoAereo {
    private final Lock lock;
    private final Condition paraDescolar;
    private final Condition paraAterrar;
    private final int NUM;
    private final int MAX;
    private boolean[] emUso;
    private int pistasLivres;
    private int descolarRequests;
    private int aterrarRequests;
    private int descolarPriority;
    private int aterrarPriority;
    private int bilhetesUsadosAterrar;
    private int bilhetesVendidosAterrar;
    private int bilhetesUsadosDescolar;
    private int bilhetesVendidosDescolar;


    public ControloTrafegoAereoImpl(int num, int max) {
        NUM = num;
        MAX = max;
        lock = new ReentrantLock();
        paraDescolar = lock.newCondition();
        paraAterrar = lock.newCondition();
        emUso = new boolean[NUM];
        for (int i = 0; i < NUM; ++i)
            emUso[i] = false;
        pistasLivres = NUM;
        descolarRequests = aterrarRequests = 0;
        descolarPriority = aterrarPriority = 0;
        bilhetesUsadosDescolar = bilhetesUsadosAterrar = 0;
        bilhetesVendidosDescolar = bilhetesVendidosAterrar = 1;
    }

    public int pedirParaDescolar() {
        lock.lock();

        int meuBilhete = bilhetesVendidosDescolar++;
        ++descolarRequests;

        while (!(meuBilhete <= bilhetesUsadosDescolar + pistasLivres && (aterrarRequests <= 0 || descolarPriority < MAX))) {
            try { paraDescolar.await(); } catch (InterruptedException ignored) {}
        }

        int pista = reservarPista();
        --descolarRequests;
        ++bilhetesUsadosDescolar;
        ++descolarPriority;
        if (descolarPriority == MAX)
            aterrarPriority = 0;

        lock.unlock();
        return pista;
    }

    public int pedirParaAterrar() {
        lock.lock();

        int meuBilhete = bilhetesVendidosAterrar++;
        ++aterrarRequests;

        while (!(meuBilhete <= bilhetesUsadosAterrar + pistasLivres && (descolarRequests <= 0 || aterrarPriority < MAX))) {
            try { paraAterrar.await(); } catch (InterruptedException ignored) {}
        }

        int pista = reservarPista();
        --aterrarRequests;
        ++bilhetesUsadosAterrar;
        ++aterrarPriority;
        if (aterrarPriority == MAX)
            descolarPriority = 0;

        lock.unlock();
        return pista;
    }

    public void descolou(int pista) {
        lock.lock();

        emUso[pista] = false;
        ++pistasLivres;
        if (aterrarRequests <= 0 || descolarPriority < MAX)
            paraDescolar.signalAll();
        else
            paraAterrar.signalAll();

        lock.unlock();
    }

    public void aterrou(int pista) {
        lock.lock();

        emUso[pista] = false;
        ++pistasLivres;
        if (descolarRequests <= 0 || aterrarPriority < MAX)
            paraAterrar.signalAll();
        else
            paraDescolar.signalAll();

        lock.unlock();
    }

    private int reservarPista() {
        for (int i = 0; i < NUM; ++i) {
            if (!emUso[i]) {
                emUso[i] = true;
                --pistasLivres;
                return i;
            }
        }

        return -1;
    }
}
