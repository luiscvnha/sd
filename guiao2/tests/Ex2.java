public class Ex2 {
    public static void main(String[] args) {
        final int NUM_CONTAS = 1;
        Ex2_Banco banco = new Ex2_Banco(NUM_CONTAS);

        Thread t1 = new Thread(new Ex2_Cliente1(banco));
        Thread t2 = new Thread(new Ex2_Cliente2(banco));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println(":(");
            e.printStackTrace();
        }

        System.out.println("Valor da conta 0: " + banco.consultar(0));
    }
}
