package Ex1;

import guiao6.Const;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket sSock = new ServerSocket(12345);

        while (true) {
            Socket clSock = sSock.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(clSock.getInputStream()));
            PrintWriter out = new PrintWriter(clSock.getOutputStream());

            String s = "";
            while (!s.equals("quit")) {
                s = in.readLine();
                out.println(s);
                out.flush();
            }

            clSock.shutdownOutput();
            clSock.shutdownInput();
            clSock.close();
        }
    }
}
