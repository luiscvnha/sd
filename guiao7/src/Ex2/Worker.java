package Ex2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class Worker implements Runnable {
    private Socket clSock;
    private Banco banco;

    public Worker(Socket s, Banco b) {
        clSock = s;
        banco = b;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clSock.getInputStream()));
            PrintWriter out = new PrintWriter(clSock.getOutputStream());
            String receive, send;
            while ((receive = in.readLine()) != null && !receive.equals("quit")) {
                String[] argv = receive.split(" ");
                try {
                    switch (argv[0]) {
                        case "criar_conta":
                            int id = banco.criarConta(Double.parseDouble(argv[1]));
                            send = "Conta " + id + " criada";
                            break;
                        case "fechar_conta":
                            try {
                                double saldo = banco.fecharConta(Integer.parseInt(argv[1]));
                                send = "Conta " + argv[1] + " com saldo " + saldo + "€ fechada";
                            } catch (ContaInvalida e) { send = e.getMessage(); }
                            break;
                        case "consultar":
                            try {
                                double saldo = banco.consultar(Integer.parseInt(argv[1]));
                                send = "Saldo: " + saldo + "€";
                            } catch (ContaInvalida e) { send = e.getMessage(); }
                            break;
                        case "consultar_total":
                            int[] contas = new int[argv.length - 1];
                            for (int i = 0; i < argv.length - 1; ++i)
                                contas[i] = Integer.parseInt(argv[i + 1]);
                            double saldoTotal = banco.consultarTotal(contas);
                            send = "Saldo total: " + saldoTotal + "€";
                            break;
                        case "levantar":
                            try {
                                if (argv.length >= 4) {
                                    String descricao = concat(argv, 3, argv.length);
                                    banco.levantar(Integer.parseInt(argv[1]), Double.parseDouble(argv[2]), descricao);
                                } else
                                    banco.levantar(Integer.parseInt(argv[1]), Double.parseDouble(argv[2]));
                                send = "Levantados " + argv[2] + "€ da conta " + argv[1];
                            } catch (ContaInvalida | SaldoInsuficiente e) { send = e.getMessage(); }
                            break;
                        case "depositar":
                            try {
                                if (argv.length >= 4) {
                                    String descricao = concat(argv, 3, argv.length);
                                    banco.depositar(Integer.parseInt(argv[1]), Double.parseDouble(argv[2]), descricao);
                                } else
                                    banco.depositar(Integer.parseInt(argv[1]), Double.parseDouble(argv[2]));
                                send = "Depositados " + argv[2] + "€ da conta " + argv[1];
                            } catch (ContaInvalida e) { send = e.getMessage(); }
                            break;
                        case "transferir":
                            try {
                                if (argv.length >= 5) {
                                    String descricao = concat(argv, 4, argv.length);
                                    banco.transferir(Integer.parseInt(argv[1]), Integer.parseInt(argv[2]), Double.parseDouble(argv[3]), descricao);
                                } else
                                    banco.transferir(Integer.parseInt(argv[1]), Integer.parseInt(argv[2]), Double.parseDouble(argv[3]));
                                send = "Transferidos " + argv[3] + "€ da conta " + argv[1] + " para a conta " + argv[2];
                            } catch (ContaInvalida | SaldoInsuficiente e) { send = e.getMessage(); }
                            break;
                        case "movimentos":
                            try {
                                List<Movimento> movimentos = banco.movimentos(Integer.parseInt(argv[1]));
                                int size = movimentos.size();
                                send = size == 0 ? "" : size + "\n" + toString(movimentos);
                            } catch (ContaInvalida e) { send = "1\n" + e.getMessage(); }
                            break;
                        default:
                            send = "Erro: Comando inválido";
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    send = "Erro: " + e.getMessage();
                }
                out.println(send);
                out.flush();
            }
        } catch (SocketException ignored) {
            // Client closed
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clSock.shutdownOutput();
                clSock.shutdownInput();
                clSock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String concat(String[] array, int inicio, int fim) {
        StringBuilder sb = new StringBuilder();
        sb.append(array[inicio]);
        for (int j = inicio+1; j < fim; ++j)
            sb.append(" ").append(array[j]);
        return sb.toString();
    }

    private <T> String toString(List<T> lista) {
        int size = lista.size();
        if (size == 0) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(lista.get(0).toString());
        for (int i = 1; i < size; ++i)
            sb.append('\n').append(lista.get(i).toString());
        return sb.toString();
    }
}
