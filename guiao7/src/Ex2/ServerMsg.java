package Ex2;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerMsg {
    public static final String EXIT = "quit";
    public static final String SEPARATOR = "|";
    public static final String SEPARATOR_REGEX = "\\|";
    public static final String MOV_VAR_SEPARATOR = " ";
    public static final String MOV_DESC_SEPARATOR = "-";

    public static class Request {
        public static final String CRIAR_CONTA = "0";
        public static final String FECHAR_CONTA = "1";
        public static final String CONSULTAR = "2";
        public static final String CONSULTAR_TOTAL = "3";
        public static final String LEVANTAR = "4";
        public static final String DEPOSITAR = "5";
        public static final String TRANSFERIR = "6";
        public static final String MOVIMENTOS = "7";
    }

    public static class Status {
        public static final String SUCCESS = "0";
        public static final String ERROR = "1";
    }

    public static class Error {
        public static final String CONTA_INVALIDA = "0";
        public static final String SALDO_INSUFICIENTE = "1";
        public static final String OUTRO = "2";
    }


    public static String createRequestMsg(String request, Object ... os) {
        StringBuilder sb = new StringBuilder();
        sb.append(request);
        for (Object o : os)
            sb.append(SEPARATOR).append(o);
        return sb.toString();
    }

    public static String createErrorMsg(String error, Object msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(Status.ERROR).append(SEPARATOR).append(error).append(SEPARATOR).append(msg);
        return sb.toString();
    }

    public static String createSuccessMsg(Object ... os) {
        StringBuilder sb = new StringBuilder();
        sb.append(Status.SUCCESS);
        for (Object o : os)
            sb.append(SEPARATOR).append(o);
        return sb.toString();
    }

    public static String listMovimentosToString(List<Movimento> lista) {
        StringBuilder sb = new StringBuilder();
        Iterator<Movimento> itr = lista.iterator();
        if (itr.hasNext()) {
            Movimento m = itr.next();
            sb.append(m.getOperacao() == Movimento.Operacao.DEPOSITO ? "D" : "L").append(MOV_VAR_SEPARATOR)
                    .append(m.getDescritivo().replace(" ", MOV_DESC_SEPARATOR)).append(MOV_VAR_SEPARATOR)
                    .append(m.getValor()).append(MOV_VAR_SEPARATOR)
                    .append(m.getSaldo());
            while (itr.hasNext()) {
                m = itr.next();
                sb.append(SEPARATOR);
                sb.append(m.getOperacao() == Movimento.Operacao.DEPOSITO ? "D" : "L").append(MOV_VAR_SEPARATOR)
                        .append(m.getDescritivo().replace(" ", MOV_DESC_SEPARATOR)).append(MOV_VAR_SEPARATOR)
                        .append(m.getValor()).append(MOV_VAR_SEPARATOR)
                        .append(m.getSaldo());
            }
        }
        return sb.toString();
    }

    public static List<Movimento> stringArrayToListMovimentos(String[] array, int inicio, int fim) {
        List<Movimento> r = new ArrayList<>();
        for (int i = inicio; i < fim; ++i) {
            String[] vars = array[i].split(MOV_VAR_SEPARATOR);
            Movimento.Operacao op = vars[0].equals("D") ? Movimento.Operacao.DEPOSITO : Movimento.Operacao.LEVANTAMENTO;
            String desc = vars[1].replace(MOV_DESC_SEPARATOR, " ");
            double valor = Double.parseDouble(vars[2]);
            double saldo = Double.parseDouble(vars[3]);
            r.add(new Movimento(op, desc, valor, saldo));
        }
        return r;
    }
}
