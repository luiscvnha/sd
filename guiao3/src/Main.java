public class Main {
    public static void main(String[] args) {
        final int NUM_CONTAS = 2;

        Banco banco = new Banco(NUM_CONTAS);
        Thread t1 = new Thread(new Cliente1(banco));
        Thread t2 = new Thread(new Cliente2(banco));

        try {
            banco.depositar(0, 5000.0);
            banco.depositar(1, 5000.0);}
        catch (ContaInvalida ci) {ci.printStackTrace();}

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ie) {ie.printStackTrace();}

        try {
            System.out.println("Valor da conta 0: " + banco.consultar(0));
            System.out.println("Valor da conta 1: " + banco.consultar(1));
        } catch (ContaInvalida ci) {ci.printStackTrace();}
    }
}
