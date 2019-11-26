package Ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

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
                                banco.levantar(Integer.parseInt(argv[1]), Double.parseDouble(argv[2]));
                                send = "Levantados " + argv[2] + "€ da conta " + argv[1];
                            } catch (ContaInvalida | SaldoInsuficiente e) { send = e.getMessage(); }
                            break;
                        case "depositar":
                            try {
                                banco.depositar(Integer.parseInt(argv[1]), Double.parseDouble(argv[2]));
                                send = "Depositados " + argv[2] + "€ da conta " + argv[1];
                            } catch (ContaInvalida e) { send = e.getMessage(); }
                            break;
                        case "transferir":
                            try {
                                banco.transferir(Integer.parseInt(argv[1]), Integer.parseInt(argv[2]), Double.parseDouble(argv[3]));
                                send = "Transferidos " + argv[3] + "€ da conta " + argv[1] + " para a conta " + argv[2];
                            } catch (ContaInvalida | SaldoInsuficiente e) { send = e.getMessage(); }
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
}
