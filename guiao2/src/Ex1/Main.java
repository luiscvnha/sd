package Ex1;

public class Main implements Runnable {
    private Counter cnt;
    private int I;

    public Main(Counter c, int i) {
        this.cnt = c;
        this.I = i;
    }

    public void run() {
        for (int i = 0; i < this.I; ++i)
            cnt.increment();
    }

    public static void main(String[] args) {
        int N = 1000,
            I = 10;

        Thread[] threads = new Thread[N];
        Counter cnt = new Counter();

        for (int i = 0; i < N; ++i)
            threads[i] = new Thread(new Main(cnt, I));

        for (int i = 0; i < N; ++i)
            threads[i].start();

        try {
            for (int i = 0; i < N; ++i)
                threads[i].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(cnt.get());
    }
}
