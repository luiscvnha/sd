package Ex1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket sSock = new ServerSocket(12345);

        Banco banco = new Banco(0);

        while (true) {
            Socket clSock = sSock.accept();

            Thread w = new Thread(new Worker(clSock, banco));
            w.start();
        }
    }
}
