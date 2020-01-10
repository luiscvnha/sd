package Ex1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);

        Banco banco = new BancoImpl(0);

        while (true) {
            Socket clientSocket = serverSocket.accept();

            new Thread(new Worker(clientSocket, banco)).start();
        }
    }
}
