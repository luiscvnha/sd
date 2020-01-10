package Ex2;

public class ContaInvalidaException extends Exception {
    private final int conta;

    public ContaInvalidaException(int id) {
        super("Conta " + id + " inválida");
        this.conta = id;
    }

    public int getConta() { return conta; }
}
