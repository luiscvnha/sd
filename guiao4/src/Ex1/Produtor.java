package Ex1;

public class Produtor implements Runnable {
    private BoundedBuffer buf;

    public Produtor(BoundedBuffer b) { buf = b; }

    @Override
    public void run() {
        for (int i = 0; i < 20; ++i)
            buf.put(i);
    }
}
