package Ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        String send;
        while (true) {
            System.out.print("client> ");
            send = stdin.readLine();
            out.println(send);
            out.flush();
            if (send.equals("quit")) break;
            else System.out.println("server> " + in.readLine());
        }

        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();
    }
}
