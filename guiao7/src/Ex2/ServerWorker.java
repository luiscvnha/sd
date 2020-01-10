package Ex2;

import static Ex2.ServerMsg.Request;
import static Ex2.ServerMsg.Error;
import static Ex2.ServerMsg.EXIT;
import static Ex2.ServerMsg.SEPARATOR_REGEX;
import static Ex2.ServerMsg.createSuccessMsg;
import static Ex2.ServerMsg.createErrorMsg;
import static Ex2.ServerMsg.listMovimentosToString;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

public class ServerWorker implements Runnable {
    private Socket clientSocket;
    private Banco banco;

    public ServerWorker(Socket s, Banco b) {
        clientSocket = s;
        banco = b;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            String received;
            while ((received = in.readLine()) != null && !received.equals(EXIT)) {
                String[] args = received.split(SEPARATOR_REGEX);
                try {
                    switch (args[0]) {
                        case Request.CRIAR_CONTA:
                            int id = banco.criarConta(Double.parseDouble(args[1]));
                            out.println(createSuccessMsg(id));
                            break;
                        case Request.FECHAR_CONTA:
                            double fc_saldo = banco.fecharConta(Integer.parseInt(args[1]));
                            out.println(createSuccessMsg(fc_saldo));
                            break;
                        case Request.CONSULTAR:
                            double c_saldo = banco.consultar(Integer.parseInt(args[1]));
                            out.println(createSuccessMsg(c_saldo));
                            break;
                        case Request.CONSULTAR_TOTAL:
                            int[] contas = new int[args.length - 1];
                            for (int i = 0; i < args.length - 1; ++i)
                                contas[i] = Integer.parseInt(args[i + 1]);
                            double ct_saldo = banco.consultarTotal(contas);
                            out.println(createSuccessMsg(ct_saldo));
                            break;
                        case Request.LEVANTAR:
                            banco.levantar(Integer.parseInt(args[1]), Double.parseDouble(args[2]), args.length >= 4 ? args[3] : "");
                            out.println(createSuccessMsg());
                            break;
                        case Request.DEPOSITAR:
                            banco.depositar(Integer.parseInt(args[1]), Double.parseDouble(args[2]), args.length >= 4 ? args[3] : "");
                            out.println(createSuccessMsg());
                            break;
                        case Request.TRANSFERIR:
                            banco.transferir(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Double.parseDouble(args[3]), args.length >= 5 ? args[4] : "");
                            out.println(createSuccessMsg());
                            break;
                        case Request.MOVIMENTOS:
                            List<Movimento> movimentos = banco.movimentos(Integer.parseInt(args[1]));
                            out.println(createSuccessMsg(listMovimentosToString(movimentos)));
                            break;
                        default:
                            throw new ComandoInvalidoException(args[0]);
                    }
                } catch (ContaInvalidaException ci) {
                    out.println(createErrorMsg(Error.CONTA_INVALIDA, ci.getConta()));
                } catch (SaldoInsuficienteException si) {
                    out.println(createErrorMsg(Error.SALDO_INSUFICIENTE, si.getConta()));
                } catch (Exception e) {
                    out.println(createErrorMsg(Error.OUTRO, e.getMessage()));
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
