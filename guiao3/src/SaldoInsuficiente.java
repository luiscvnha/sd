public class SaldoInsuficiente extends Exception {
    public SaldoInsuficiente() { super(); }

    public SaldoInsuficiente(int id) {
    	super("Conta " + id + " com saldo insuficiente.");
    }
}
