package Ex3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket sSock = new ServerSocket(12345);

        while (true) {
            Socket clSock = sSock.accept();

            Thread c = new Thread(new Connection(clSock));
            c.start();
        }
    }
}
