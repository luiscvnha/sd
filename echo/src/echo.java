import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class echo {
    public static void main(String[] args) {
        BufferedReader i = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            while((line = i.readLine()) != null)
                System.out.println(line);
        } catch (IOException e) {
            System.err.println("Error reading line from System.in!");
            e.printStackTrace();
        }
    }
}
