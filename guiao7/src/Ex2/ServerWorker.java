package Ex2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;


public class ServerWorker implements Runnable {
    private Socket clSock;
    private Banco banco;


    public ServerWorker(Socket s, Banco b) {
        clSock = s;
        banco = b;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clSock.getInputStream()));
            PrintWriter out = new PrintWriter(clSock.getOutputStream());
            String receive, send;
            while ((receive = in.readLine()) != null && !receive.equals(ServerMsg.EXIT)) {
                String[] args = receive.split(ServerMsg.SEPARATOR_REGEX);
                try {
                    switch (args[0]) {
                        case ServerMsg.Request.CRIAR_CONTA:
                            int id = banco.criarConta(Double.parseDouble(args[1]));
                            send = ServerMsg.createSuccessMsg(id);
                            break;
                        case ServerMsg.Request.FECHAR_CONTA:
                            double fc_saldo = banco.fecharConta(Integer.parseInt(args[1]));
                            send = ServerMsg.createSuccessMsg(fc_saldo);
                            break;
                        case ServerMsg.Request.CONSULTAR:
                            double c_saldo = banco.consultar(Integer.parseInt(args[1]));
                            send = ServerMsg.createSuccessMsg(c_saldo);
                            break;
                        case ServerMsg.Request.CONSULTAR_TOTAL:
                            int[] contas = new int[args.length - 1];
                            for (int i = 0; i < args.length - 1; ++i)
                                contas[i] = Integer.parseInt(args[i + 1]);
                            double ct_saldo = banco.consultarTotal(contas);
                            send = ServerMsg.createSuccessMsg(ct_saldo);
                            break;
                        case ServerMsg.Request.LEVANTAR:
                            banco.levantar(Integer.parseInt(args[1]), Double.parseDouble(args[2]), args.length >= 4 ? args[3] : "");
                            send = ServerMsg.createSuccessMsg();
                            break;
                        case ServerMsg.Request.DEPOSITAR:
                            banco.depositar(Integer.parseInt(args[1]), Double.parseDouble(args[2]), args.length >= 4 ? args[3] : "");
                            send = ServerMsg.createSuccessMsg();
                            break;
                        case ServerMsg.Request.TRANSFERIR:
                            banco.transferir(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Double.parseDouble(args[3]), args.length >= 5 ? args[4] : "");
                            send = ServerMsg.createSuccessMsg();
                            break;
                        case ServerMsg.Request.MOVIMENTOS:
                            List<Movimento> movimentos = banco.movimentos(Integer.parseInt(args[1]));
                            send = ServerMsg.createSuccessMsg(ServerMsg.listMovimentosToString(movimentos));
                            break;
                        default:
                            throw new ComandoInvalidoException(args[0]);
                    }
                } catch (ContaInvalidaException ci) {
                    send = ServerMsg.createErrorMsg(ServerMsg.Error.CONTA_INVALIDA, ci.getConta());
                } catch (SaldoInsuficienteException si) {
                    send = ServerMsg.createErrorMsg(ServerMsg.Error.SALDO_INSUFICIENTE, si.getConta());
                } catch (Exception e) {
                    send = ServerMsg.createErrorMsg(ServerMsg.Error.OUTRO, e.getMessage());
                }
                System.out.println(send);
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
