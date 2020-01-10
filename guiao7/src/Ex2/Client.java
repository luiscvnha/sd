package Ex2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Client {
    public static void main(String[] ignored) throws IOException {
        Banco banco = new BancoRemoto("localhost", 12345);

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        String cmd;
        boolean stop = false;
        while (!stop) {
            System.out.print("[client]$ ");
            cmd = stdin.readLine();
            String[] argv = cmd.split(" ");
            try {
                switch (argv[0]) {
                    case ServerMsg.EXIT:
                        banco.close();
                        stop = true;
                        break;
                    case "criar_conta":
                        int id = banco.criarConta(Double.parseDouble(argv[1]));
                        System.out.println("Conta " + id + " criada");
                        break;
                    case "fechar_conta":
                        double fc_saldo = banco.fecharConta(Integer.parseInt(argv[1]));
                        System.out.println("Conta " + argv[1] + " com saldo " + fc_saldo + "€ fechada");
                        break;
                    case "consultar":
                        double c_saldo = banco.consultar(Integer.parseInt(argv[1]));
                        System.out.println("Saldo: " + c_saldo + "€");
                        break;
                    case "consultar_total":
                        int[] contas = new int[argv.length - 1];
                        for (int i = 0; i < argv.length - 1; ++i)
                            contas[i] = Integer.parseInt(argv[i + 1]);
                        double ct_saldo = banco.consultarTotal(contas);
                        System.out.println("Saldo total: " + ct_saldo + "€");
                        break;
                    case "levantar":
                        banco.levantar(Integer.parseInt(argv[1]), Double.parseDouble(argv[2]),
                                argv.length >= 4 ? concat(argv, 3, argv.length) : "");
                        System.out.println("Levantados " + argv[2] + "€ da conta " + argv[1]);
                        break;
                    case "depositar":
                        banco.depositar(Integer.parseInt(argv[1]), Double.parseDouble(argv[2]),
                                argv.length >= 4 ? concat(argv, 3, argv.length) : "");
                        System.out.println("Depositados " + argv[2] + "€ na conta " + argv[1]);
                        break;
                    case "transferir":
                        banco.transferir(Integer.parseInt(argv[1]), Integer.parseInt(argv[2]), Double.parseDouble(argv[3]),
                                argv.length >= 5 ? concat(argv, 4, argv.length) : "");
                        System.out.println("Transferidos " + argv[3] + "€ da conta " + argv[1] + " para a conta " + argv[2]);
                        break;
                    case "movimentos":
                        List<Movimento> movimentos = banco.movimentos(Integer.parseInt(argv[1]));
                        for (Movimento m : movimentos)
                            System.out.println(m);
                        break;
                    default:
                        throw new ComandoInvalidoException(argv[0]);
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static String concat(String[] array, int inicio, int fim) {
        StringBuilder sb = new StringBuilder();
        sb.append(array[inicio]);
        for (int j = inicio+1; j < fim; ++j)
            sb.append(" ").append(array[j]);
        return sb.toString();
    }
}
