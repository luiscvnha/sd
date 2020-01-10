package Ex3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);

        while (true) {
            Socket clientSocket = serverSocket.accept();

            Runnable r = new Worker(clientSocket);
            Thread t = new Thread(r);
            t.start();
        }
    }
}
