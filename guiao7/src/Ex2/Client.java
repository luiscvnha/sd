package Ex2;

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

        String send, receive;
        while (true) {
            System.out.print("client> ");
            send = stdin.readLine();
            out.println(send);
            out.flush();
            if (send.equals("quit")) break;
            else {
                receive = in.readLine();
                if (send.split(" ")[0].equals("movimentos")) {
                    try {
                        int N = Integer.parseInt(receive);
                        System.out.println("server>");
                        for (int i = 0; i < N; ++i)
                            System.out.println(in.readLine());
                    } catch (NumberFormatException e) {
                        System.out.println("server> " + receive);
                    }
                } else
                    System.out.println("server> " + receive);
            }
        }

        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();
    }
}
