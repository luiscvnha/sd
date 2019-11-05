package Ex1;

public class Main {
    public static void main(String[] args) {
        BoundedBuffer buf = new BoundedBuffer(10);
        Thread t1 = new Thread(new Consumidor(buf));
        Thread t2 = new Thread(new Produtor(buf));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(buf.toString());
    }
}
