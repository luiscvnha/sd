package Ex2;

public enum ID_Operacao {
    DEPOSITO("Depósito"),
    LEVANTAMENTO("Levantamento");

    private final String desc;

    private ID_Operacao(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return desc;
    }
}
