package Ex2;

import Ex1.BoundedBuffer;

public class Consumidor implements Runnable {
    private BoundedBuffer buf;
    private int numOps;
    private int Tc;

    public Consumidor(BoundedBuffer b, int n, int t) {
        buf = b;
        numOps = n;
        Tc = t;
    }

    @Override
    public void run() {
        for (int i = 0; i < numOps; ++i) {
            buf.get();
            try {
                Thread.sleep(Tc);
            } catch (InterruptedException e) {e.printStackTrace();}
        }
    }
}
