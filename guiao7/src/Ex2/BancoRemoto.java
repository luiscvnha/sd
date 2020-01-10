package Ex2;

import static Ex2.ServerMsg.Request;
import static Ex2.ServerMsg.Status;
import static Ex2.ServerMsg.Error;
import static Ex2.ServerMsg.EXIT;
import static Ex2.ServerMsg.SEPARATOR_REGEX;
import static Ex2.ServerMsg.createRequestMsg;
import static Ex2.ServerMsg.stringArrayToListMovimentos;

import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

public class BancoRemoto implements Banco {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public BancoRemoto(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
    }

    public void close() throws IOException {
        out.println(EXIT);
        out.flush();
        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();
    }

    public int criarConta(double saldoInicial) throws BancoRemotoException {
        out.println(createRequestMsg(Request.CRIAR_CONTA, saldoInicial));
        out.flush();

        String[] args;
        try {
            args = in.readLine().split(SEPARATOR_REGEX);
        } catch (IOException e) {
            throw new BancoRemotoException(e.getMessage());
        }

        if (args[0].equals(Status.SUCCESS))
            return Integer.parseInt(args[1]);
        else
            throw new BancoRemotoException(args[2]);
    }

    public double fecharConta(int id) throws ContaInvalidaException, BancoRemotoException {
        out.println(createRequestMsg(Request.FECHAR_CONTA, id));
        out.flush();

        String[] args;
        try {
            args = in.readLine().split(SEPARATOR_REGEX);
        } catch (IOException e) {
            throw new BancoRemotoException(e.getMessage());
        }

        if (args[0].equals(Status.SUCCESS))
            return Double.parseDouble(args[1]);
        else if (args[1].equals(Error.CONTA_INVALIDA))
            throw new ContaInvalidaException(Integer.parseInt(args[2]));
        else
            throw new BancoRemotoException(args[2]);
    }

    public double consultar(int id) throws ContaInvalidaException, BancoRemotoException {
        out.println(createRequestMsg(Request.CONSULTAR, id));
        out.flush();

        String[] args;
        try {
            args = in.readLine().split(SEPARATOR_REGEX);
        } catch (IOException e) {
            throw new BancoRemotoException(e.getMessage());
        }

        if (args[0].equals(Status.SUCCESS))
            return Double.parseDouble(args[1]);
        else if (args[1].equals(Error.CONTA_INVALIDA))
            throw new ContaInvalidaException(Integer.parseInt(args[2]));
        else
            throw new BancoRemotoException(args[2]);
    }

    public double consultarTotal(int[] id_contas) throws BancoRemotoException {
        Object[] array = new Integer[id_contas.length];
        int i = 0;
        for (int x : id_contas)
            array[i++] = x;

        out.println(createRequestMsg(Request.CONSULTAR_TOTAL, array));
        out.flush();

        String[] args;
        try {
            args = in.readLine().split(SEPARATOR_REGEX);
        } catch (IOException e) {
            throw new BancoRemotoException(e.getMessage());
        }

        if (args[0].equals(Status.SUCCESS))
            return Double.parseDouble(args[1]);
        else
            throw new BancoRemotoException(args[1]);
    }

    public void levantar(int id, double valor, String descricao) throws ContaInvalidaException, SaldoInsuficienteException, BancoRemotoException {
        out.println(createRequestMsg(Request.LEVANTAR, id, valor, descricao));
        out.flush();

        String[] args;
        try {
            args = in.readLine().split(SEPARATOR_REGEX);
        } catch (IOException e) {
            throw new BancoRemotoException(e.getMessage());
        }

        if (args[0].equals(Status.SUCCESS));
        else if (args[1].equals(Error.CONTA_INVALIDA))
            throw new ContaInvalidaException(Integer.parseInt(args[2]));
        else if (args[1].equals(Error.SALDO_INSUFICIENTE))
            throw new SaldoInsuficienteException(Integer.parseInt(args[2]));
        else
            throw new BancoRemotoException(args[2]);
    }

    public void depositar(int id, double valor, String descricao) throws ContaInvalidaException, BancoRemotoException {
        out.println(createRequestMsg(Request.DEPOSITAR, id, valor, descricao));
        out.flush();

        String[] args;
        try {
            args = in.readLine().split(SEPARATOR_REGEX);
        } catch (IOException e) {
            throw new BancoRemotoException(e.getMessage());
        }

        if (args[0].equals(Status.SUCCESS));
        else if (args[1].equals(Error.CONTA_INVALIDA))
            throw new ContaInvalidaException(Integer.parseInt(args[2]));
        else
            throw new BancoRemotoException(args[2]);
    }

    public void transferir(int id_origem, int id_destino, double valor, String descricao) throws ContaInvalidaException, SaldoInsuficienteException, BancoRemotoException {
        out.println(createRequestMsg(Request.TRANSFERIR, id_origem, id_destino, valor, descricao));
        out.flush();

        String[] args;
        try {
            args = in.readLine().split(SEPARATOR_REGEX);
        } catch (IOException e) {
            throw new BancoRemotoException(e.getMessage());
        }

        if (args[0].equals(Status.SUCCESS));
        else if (args[1].equals(Error.CONTA_INVALIDA))
            throw new ContaInvalidaException(Integer.parseInt(args[2]));
        else if (args[1].equals(Error.SALDO_INSUFICIENTE))
            throw new SaldoInsuficienteException(Integer.parseInt(args[2]));
        else
            throw new BancoRemotoException(args[2]);
    }

    public List<Movimento> movimentos(int id) throws ContaInvalidaException, BancoRemotoException {
        out.println(createRequestMsg(Request.MOVIMENTOS, id));
        out.flush();

        String[] args;
        try {
            args = in.readLine().split(SEPARATOR_REGEX);
        } catch (IOException e) {
            throw new BancoRemotoException(e.getMessage());
        }

        if (args[0].equals(Status.SUCCESS))
            return stringArrayToListMovimentos(args, 1, args.length);
        else if (args[1].equals(Error.CONTA_INVALIDA))
            throw new ContaInvalidaException(Integer.parseInt(args[2]));
        else
            throw new BancoRemotoException(args[2]);
    }
}
