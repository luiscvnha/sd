package Ex2;

import Ex1.Controlador;
import Ex1.ControladorImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);

        Controlador control = new ControladorImpl();

        while (true) {
            Socket clientSocket = serverSocket.accept();

            Worker w = new Worker(clientSocket, control);
            new Thread(w).start();
        }
    }
}
