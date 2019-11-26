package Ex2;

public class Movimento {
    private ID_Operacao operacao;
    private String descritivo;
    private double valor;
    private double saldo;

    public Movimento(ID_Operacao operacao, String descritivo, double valor, double saldo) {
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
