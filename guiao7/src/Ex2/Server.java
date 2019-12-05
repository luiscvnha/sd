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

            ServerWorker sw = new ServerWorker(clSock, banco);
            Thread t = new Thread(sw);
            t.start();
        }
    }
}
