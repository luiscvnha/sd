class CounterA {
    private int x;

    public CounterA() { this.x = 0; }

    public synchronized void increment() { ++this.x; }

    public int get() { return this.x; }
}


public class Ex2_1 implements Runnable {
    private CounterA cnt;
    private int I;

    public Ex2_1(CounterA c, int i) {
        this.cnt = c;
        this.I = i;
    }

    public void run() {
        for (int i = 0; i < I; ++i)
            cnt.increment();
    }

    public static void main(String[] args) {
        int N = 1000,
            I = 10;

        Thread[] threads = new Thread[N];
        CounterA cnt = new CounterA();

        for (int i = 0; i < N; ++i)
            threads[i] = new Thread(new Ex2_1(cnt, I));

        for (int i = 0; i < N; ++i)
            threads[i].start();

        try {
            for (int i = 0; i < N; ++i)
                threads[i].join();
        } catch (InterruptedException e) {System.out.println(":(");}

        System.out.println(cnt.get());
    }
}
