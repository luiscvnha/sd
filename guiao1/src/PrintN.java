public class PrintN implements Runnable {
    private int N;

    public PrintN(int N) { this.N = N; }

    public void run() {
        for (int i = 1; i <= N; ++i)
            System.out.println(i);
    }
}
