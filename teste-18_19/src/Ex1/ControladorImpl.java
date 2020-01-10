package Ex1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;


public class ControladorImpl implements Controlador {
    private final Lock lock;
    private final Condition[] espera_entrar;          // espera_entrar[i]: condição das pessoas à espera do autocarro no terminal i+1
    private final Condition[] espera_sair;            // espera_sair[i]: condição dos passageiros à espera de sair no terminal i+1
    private final Condition passageiros_a_entrar;     // condição dos passageiros a entrar
    private final Condition passageiros_a_sair;       // condição dos passageiros a sair
    private int terminal;                       // > 0: terminal atual | < 0: em viagem, próximo terminal
    private int ocupacao;                       // ocupação do autocarro
    private int[] pDestino;                     // pDestino[i]: passageiros à espera do terminal i+1
    private int[] pEspera;                      // pEspera[i]: pessoas à espera no terminal i+1
    private int[] bilhetesVendidos;             // bilhetesVendidos[i] = próximo bilhete a ser vendido no terminal i+1
    private int[] bilhetesUsados;               // bilhetesUsados[i] = último bilhete usado no terminal i+1

    private static final int NUM_TERMINAIS = 5;                                 // [1, 5]
    private static final int CAPACIDADE_MAX = 30;                               // 30 passageiros
    private static final int OCUPACAO_MIN = 10;                                 // 10 passageiros
    private static final int TEMPO_ENTRE_TERMINAIS_EM_MILLIS = 5 * 60 * 1000;    // 5 min.


    public ControladorImpl() {
        this.lock = new ReentrantLock();
        this.espera_entrar = new Condition[NUM_TERMINAIS];
        this.espera_sair = new Condition[NUM_TERMINAIS];
        this.passageiros_a_entrar = lock.newCondition();
        this.passageiros_a_sair = lock.newCondition();
        this.terminal = -1;
        this.ocupacao = 0;
        this.pDestino = new int[NUM_TERMINAIS];
        this.pEspera = new int[NUM_TERMINAIS];
        this.bilhetesVendidos = new int[NUM_TERMINAIS];
        this.bilhetesUsados = new int[NUM_TERMINAIS];
        for (int i = 0; i < NUM_TERMINAIS; ++i) {
            pDestino[i] = 0;
            pEspera[i] = 0;
            bilhetesVendidos[i] = 1;
            bilhetesUsados[i] = 0;
            espera_entrar[i] = lock.newCondition();
            espera_sair[i] = lock.newCondition();
        }
    }

    public void requisita_viagem(int origem /*[1, 5]*/, int destino /*[1, 5]*/) {
        lock.lock();

        // chega ao terminal
        ++pEspera[origem-1];
        // compra bilhete
        int meuBilhete = bilhetesVendidos[origem-1]++;

        // espera pelo autocarro
        while (terminal != origem || ocupacao >= CAPACIDADE_MAX
                || meuBilhete > bilhetesUsados[origem-1] + CAPACIDADE_MAX - ocupacao) {
            try { espera_entrar[origem-1].await(); } catch (InterruptedException ignored) {}
        }
        // entra no autocarro

        // deixa de esperar pelo autocarro
        --pEspera[origem-1];
        // usa o bilhete
        ++bilhetesUsados[origem-1];
        // espera para sair no terminal 'destino'
        ++pDestino[destino-1];
        // ocupa lugar
        ++ocupacao;
        // avisa o motorista de ter entrado
        passageiros_a_entrar.signal();

        lock.unlock();
    }

    public void espera(int destino /*[1, 5]*/) {
        lock.lock();

        // espera que chegue ao terminal 'destino'
        while (terminal != destino) {
            try { espera_sair[destino-1].await(); } catch (InterruptedException ignored) {}
        }
        // sai do autocarro

        // deixa de esperar pelo terminal 'destino'
        --pDestino[destino-1];
        // liberta o lugar
        --ocupacao;
        // avisa o motorista de ter saído
        passageiros_a_sair.signal();

        lock.unlock();
    }

    public void parte() {
        lock.lock();

        // chega ao terminal
        terminal = -terminal;

        // passageiros saem
        espera_sair[terminal-1].signalAll();
        while (pDestino[terminal-1] != 0) {
            try { passageiros_a_sair.await(); } catch (InterruptedException ignored) {}
        }

        // pessoas entram
        espera_entrar[terminal-1].signalAll();

        // espera para arrancar
        while (!(ocupacao >= CAPACIDADE_MAX || (ocupacao >= OCUPACAO_MIN && pEspera[terminal-1] <= 0))) {
            try { passageiros_a_entrar.await(); } catch (InterruptedException ignored) {}
        }

        // calcula o próximo terminal
        terminal = terminal == NUM_TERMINAIS ? 1 : terminal+1;
        // parte
        terminal = -terminal;

        lock.unlock();
    }
}
