public class Ex1 implements Runnable {
    private int N;

    public Ex1(int n) { this.N = n; }

    public void run() {
        for (int i = 1; i <= N; ++i)
            System.out.println(i);
    }

    public static void main(String [] args){
        int N = 5,
            I = 4;
        Thread[] threads = new Thread[N];

        for (int i = 0; i < N; ++i)
            threads[i] = new Thread(new PrintN(I));

        System.out.println("Antes");
        for (int i = 0; i < N; ++i)
            threads[i].start();
        System.out.println("Depois");

        try {
            for (int i = 0; i < N; ++i)
                threads[i].join();
        } catch (InterruptedException e) {System.out.println(":(");}
        System.out.println("Fim");
    }
}
