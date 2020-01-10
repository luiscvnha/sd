package Ex2;

import Ex1.ControloTrafegoAereo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Worker implements Runnable {
    private final Socket clientSocket;
    private final ControloTrafegoAereo controlo;


    public Worker(Socket clientSocket, ControloTrafegoAereo controlo) {
        this.clientSocket = clientSocket;
        this.controlo = controlo;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            String received;
            while ((received = in.readLine()) != null) {
                try {
                    switch (received) {
                        case "pedirParaDeslocar":
                            int pista0 = controlo.pedirParaDescolar();
                            out.println("Pista: " + pista0);
                            break;
                        case "pedirParaAterrar":
                            int pista1 = controlo.pedirParaAterrar();
                            out.println("Pista: " + pista1);
                            break;
                        case "descolou":
                            controlo.descolou(Integer.parseInt(in.readLine()));
                            out.println("Sucesso");
                            break;
                        case "aterrou":
                            controlo.aterrou(Integer.parseInt(in.readLine()));
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
