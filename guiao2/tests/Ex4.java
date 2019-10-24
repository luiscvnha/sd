public class Ex4 {
    public static void main(String[] args) {
        final int NUM_CONTAS = 2;

        Ex4_Banco banco = new Ex4_Banco(NUM_CONTAS);
        Thread t1 = new Thread(new Ex4_Cliente1(banco));
        Thread t2 = new Thread(new Ex4_Cliente2(banco));

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
        System.out.println("Valor da conta 1: " + banco.consultar(1));
    }
}
