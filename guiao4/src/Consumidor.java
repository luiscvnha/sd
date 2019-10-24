public class Consumidor implements Runnable {
    private BoundedBuffer buf;

    public Consumidor(BoundedBuffer b) {
        buf = b;
    }

    @Override
    public void run() {
        for (int i = 0; i < 15; ++i)
            buf.get();
    }
}
