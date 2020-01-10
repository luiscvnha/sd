package Ex2;

public class SaldoInsuficienteException extends Exception {
    private final int conta;

    public SaldoInsuficienteException(int id) {
        super("Conta " + id + " com saldo insuficiente");
        this.conta = id;
    }

    public int getConta() { return conta; }
}
