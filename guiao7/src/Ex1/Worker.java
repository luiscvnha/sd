package Ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Worker implements Runnable {
    private Socket clientSocket;
    private Banco banco;

    public Worker(Socket s, Banco b) {
        clientSocket = s;
        banco = b;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            String received;
            while ((received = in.readLine()) != null && !received.equals("quit")) {
                String[] args = received.split(" ");
                try {
                    switch (args[0]) {
                        case "criar_conta":
                            int id = banco.criarConta(Double.parseDouble(args[1]));
                            out.println("Conta " + id + " criada");
                            break;
                        case "fechar_conta":
                            try {
                                double saldo = banco.fecharConta(Integer.parseInt(args[1]));
                                out.println("Conta " + args[1] + " com saldo " + saldo + "€ fechada");
                            } catch (ContaInvalida e) { out.println(e.getMessage()); }
                            break;
                        case "consultar":
                            try {
                                double saldo = banco.consultar(Integer.parseInt(args[1]));
                                out.println("Saldo: " + saldo + "€");
                            } catch (ContaInvalida e) { out.println(e.getMessage()); }
                            break;
                        case "consultar_total":
                            int[] contas = new int[args.length - 1];
                            for (int i = 0; i < args.length - 1; ++i)
                                contas[i] = Integer.parseInt(args[i + 1]);
                            double saldoTotal = banco.consultarTotal(contas);
                            out.println("Saldo total: " + saldoTotal + "€");
                            break;
                        case "levantar":
                            try {
                                banco.levantar(Integer.parseInt(args[1]), Double.parseDouble(args[2]));
                                out.println("Levantados " + args[2] + "€ da conta " + args[1]);
                            } catch (ContaInvalida | SaldoInsuficiente e) { out.println(e.getMessage()); }
                            break;
                        case "depositar":
                            try {
                                banco.depositar(Integer.parseInt(args[1]), Double.parseDouble(args[2]));
                                out.println("Depositados " + args[2] + "€ da conta " + args[1]);
                            } catch (ContaInvalida e) { out.println(e.getMessage()); }
                            break;
                        case "transferir":
                            try {
                                banco.transferir(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Double.parseDouble(args[3]));
                                out.println("Transferidos " + args[3] + "€ da conta " + args[1] + " para a conta " + args[2]);
                            } catch (ContaInvalida | SaldoInsuficiente e) { out.println(e.getMessage()); }
                            break;
                        default:
                            out.println("Erro: Comando inválido");
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    out.println("Erro: " + e.getMessage());
                }
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.shutdownOutput();
                clientSocket.shutdownInput();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
