package Ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
            String command, message;
            while ((command = in.readLine()) != null && !command.equals("quit")) {
                String[] argv = command.split(" ");
                switch (argv[0]) {
                    case "criar_conta":
                        int id = banco.criarConta(Double.parseDouble(argv[1]));
                        message = "Conta " + id + " criada";
                    break;
                    case "fechar_conta":
                        try {
                            double saldo = banco.fecharConta(Integer.parseInt(argv[1]));
                            message = "Conta " + argv[1] + " com saldo " + saldo + "€ fechada";
                        } catch (ContaInvalida e) { message = e.getMessage(); }
                    break;
                    case "consultar":
                        try {
                            double saldo = banco.consultar(Integer.parseInt(argv[1]));
                            message = "Saldo: " + saldo + "€";
                        } catch (ContaInvalida e) { message = e.getMessage(); }
                    break;
                    case "consultar_total":
                        int[] contas = new int[argv.length-1];
                        for (int i = 0; i < argv.length-1; ++i)
                            contas[i] = Integer.parseInt(argv[i+1]);
                        double saldoTotal = banco.consultarTotal(contas);
                        message = "Saldo total: " + saldoTotal + "€";
                    break;
                    case "levantar":
                        try {
                            banco.levantar(Integer.parseInt(argv[1]), Double.parseDouble(argv[2]));
                            message = "Levantados " + argv[2] + "€ da conta " + argv[1];
                        } catch (ContaInvalida | SaldoInsuficiente e) { message = e.getMessage(); }
                    break;
                    case "depositar":
                        try {
                            banco.depositar(Integer.parseInt(argv[1]), Double.parseDouble(argv[2]));
                            message = "Depositados " + argv[2] + "€ da conta " + argv[1];
                        } catch (ContaInvalida e) { message = e.getMessage(); }
                    break;
                    case "transferir":
                        try {
                            banco.transferir(Integer.parseInt(argv[1]), Integer.parseInt(argv[2]), Double.parseDouble(argv[3]));
                            message = "Transferidos " + argv[3] + "€ da conta " + argv[1] + " para a conta " + argv[2];
                        } catch (ContaInvalida | SaldoInsuficiente e) { message = e.getMessage(); }
                    break;
                    default:
                        message = "Erro";
                    break;
                }
                out.println(message);
                out.flush();
            }
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
