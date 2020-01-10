package Ex2;

import Ex1.BoundedBuffer;

public class Produtor implements Runnable {
    private BoundedBuffer buf;
    private int numOps;
    private int Tp;

    public Produtor(BoundedBuffer b, int n, int t) {
        buf = b;
        numOps = n;
        Tp = t;
    }

    public void run() {
        for (int i = 0; i < numOps; ++i) {
            buf.put(i);
            try {
                Thread.sleep(Tp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
