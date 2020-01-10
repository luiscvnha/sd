package Ex2;

import Ex1.ControloTrafegoAereo;
import Ex1.ControloTrafegoAereoImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {
    private static final int NUM_PISTAS = 10;
    private static final int MAX_PRIORITY = 3;


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);

        ControloTrafegoAereo controlo = new ControloTrafegoAereoImpl(NUM_PISTAS, MAX_PRIORITY);

        while (true) {
            Socket clientSocket = serverSocket.accept();

            Worker w = new Worker(clientSocket, controlo);
            new Thread(w).start();
        }
    }
}
