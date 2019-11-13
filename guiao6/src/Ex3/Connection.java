package Ex3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection implements Runnable {
    private Socket clSock;

    public Connection(Socket s) {
        clSock = s;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clSock.getInputStream()));
            PrintWriter out = new PrintWriter(clSock.getOutputStream());
            String s = "";
            while (!s.equals("quit")) {
                s = in.readLine();
                out.println(s);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clSock.shutdownOutput();
                clSock.shutdownInput();
                clSock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
