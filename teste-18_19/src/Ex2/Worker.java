package Ex2;

import Ex1.Controlador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Worker implements Runnable {
    private final Socket clientSocket;
    private final Controlador controlador;


    public Worker(Socket clientSocket, Controlador controlador) {
        this.clientSocket = clientSocket;
        this.controlador = controlador;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            String received;
            while ((received = in.readLine()) != null) {
                try {
                    switch (received) {
                        case "requisita_viagem":
                            int origem0 = Integer.parseInt(in.readLine());
                            int destino0 = Integer.parseInt(in.readLine());
                            controlador.requisita_viagem(origem0, destino0);
                            out.println("Sucesso");
                            break;
                        case "espera":
                            int destino1 = Integer.parseInt(in.readLine());
                            controlador.espera(destino1);
                            out.println("Sucesso");
                            break;
                        default:
                            out.println("Operação inválida");
                    }
                } catch (NumberFormatException e) {
                    out.println("Erro: " + e.getMessage());
                }
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.shutdownOutput();
                clientSocket.shutdownInput();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
