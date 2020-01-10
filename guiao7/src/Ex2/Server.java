package Ex2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket sSock = new ServerSocket(12345);

        Banco banco = new BancoLocal(0);

        while (true) {
            Socket clSock = sSock.accept();

            new Thread(new ServerWorker(clSock, banco)).start();
        }
    }
}
