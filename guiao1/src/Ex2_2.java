class CounterB {
    public int x;

    public CounterB() { this.x = 0; }
}


public class Ex2_2 implements Runnable {
    private CounterB cnt;
    private int I;

    public Ex2_2(CounterB c, int i) {
        this.cnt = c;
        this.I = i;
    }

    public void run() {
        for (int i = 0; i < I; ++i) {
            synchronized (this.cnt) {
                ++cnt.x;
            }
        }
    }

    public static void main(String[] args) {
        int N = 1000,
            I = 10;

        Thread[] threads = new Thread[N];
        CounterB cnt = new CounterB();

        for (int i = 0; i < N; ++i)
            threads[i] = new Thread(new Ex2_2(cnt, I));

        for (int i = 0; i < N; ++i)
            threads[i].start();

        try {
            for (int i = 0; i < N; ++i)
                threads[i].join();
        } catch (InterruptedException e) {System.out.println(":(");}

        System.out.println(cnt.x);
    }
}
