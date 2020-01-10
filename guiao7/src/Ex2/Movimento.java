package Ex2;

public class Movimento {
    private Operacao operacao;
    private String descritivo;
    private double valor;
    private double saldo;

    public enum Operacao {
        DEPOSITO("Depósito"),
        LEVANTAMENTO("Levantamento");

        private final String desc;

        private Operacao(String desc) {
            this.desc = desc;
        }

        public String toString() {
            return desc;
        }
    }


    public Movimento(Operacao operacao, String descritivo, double valor, double saldo) {
        this.operacao = operacao;
        this.descritivo = descritivo;
        this.valor = valor;
        this.saldo = saldo;
    }

    private Movimento(Movimento m) {
        this.operacao = m.operacao;
        this.descritivo = m.descritivo;
        this.valor = m.valor;
        this.saldo = m.saldo;
    }

    public Operacao getOperacao() { return operacao; }
    public String getDescritivo() { return descritivo; }
    public double getValor() { return valor; }
    public double getSaldo() { return saldo; }

    public Movimento clone() { return new Movimento(this); }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Movimento {");
        sb.append("operação=").append(operacao);
        sb.append(", descritivo='").append(descritivo);
        sb.append("', valor=").append(valor);
        sb.append(", saldo=").append(saldo);
        sb.append("}");
        return sb.toString();
    }
}
