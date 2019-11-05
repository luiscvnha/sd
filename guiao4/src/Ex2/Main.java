package Ex2;

import Ex1.BoundedBuffer;

public class Main {
    public static void main(String[] args) {
        BoundedBuffer buf = new BoundedBuffer(10);       // iniciar buffer com 10 posições
        int Tc = 50;                                        // tempo de consumo em ms (0.5s)
        int Tp = 100;                                       // tempo de produção em ms (1s)
        int total_ops = 100;                                // no total, produzir 100 items e consumir 100 items
        int N = 10;                                         // número total de threads
        int P;                                              // número de produtores
        int C;                                              // número de consumidores
        double maxDebito = 0.0;                             // débito máximo
        int maxProd = 0;                                    // guarda o número de produtores referente ao débito máximo observado

        for (P = 1; P <= 9; ++P) { // testar várias combinações possíveis de produtores e consumidores até achar o débito máximo
            C = N - P;
            Thread[] prod = new Thread[P];
            Thread[] cons = new Thread[C];
            int ops_per_prod = total_ops/P, remain_prod = total_ops % P;
            int ops_per_cons = total_ops/C, remain_cons = total_ops % C;

            for (int i = 0; i < P-1; ++i) // criar threads produtor
                prod[i] = new Thread(new Produtor(buf, ops_per_prod, Tp));
            prod[P-1] = new Thread(new Produtor(buf, ops_per_prod + remain_prod, Tp));

            for (int i = 0; i < C-1; ++i) // criar threads consumidor
                cons[i] = new Thread(new Consumidor(buf, ops_per_cons, Tc));
            cons[C-1] = new Thread(new Consumidor(buf, ops_per_cons + remain_cons, Tc));

            double startTime = (double) System.currentTimeMillis() / 1000.0; // iniciar cronómetro e threads
            for (int i = 0; i < P; ++i) // iniciar produtores
                prod[i].start();
            for (int i = 0; i < C; ++i) // iniciar consumidores
                cons[i].start();

            for (int i = 0; i < P; ++i) // aguardar que produtores finalizem
                try {
                    prod[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            for (int i = 0; i < C; ++i) // aguardar que consumidores finalizem
                try {
                    cons[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            double endTime = (double) System.currentTimeMillis() / 1000.0; // parar cronómetro e calcular débito

            double debito = (double) total_ops / (endTime - startTime);

            System.out.println("Débito com " + P + " produtor(es) e " + C + " consumidor(es): " + debito + " ops/s");

            if (debito > maxDebito) {
                maxDebito = debito;
                maxProd = P;
            }
        }

        System.out.println("\nMelhor débito: " + maxProd + " produtor(es) e " + (N-maxProd) + " consumidor(es) | " + maxDebito + " ops/s");
    }
}
