package Ex2;

public class Main {
    public static void main(String[] args) {
        final int NUM_CONTAS = 1;
        Banco banco = new Banco(NUM_CONTAS);

        Thread t1 = new Thread(new Cliente1(banco));
        Thread t2 = new Thread(new Cliente2(banco));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Valor da conta 0: " + banco.consultar(0));
    }
}
