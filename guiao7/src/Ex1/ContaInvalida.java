package Ex1;

public class ContaInvalida extends Exception {
    public ContaInvalida() { super(); }

    public ContaInvalida(int id) { super("Conta " + id + " inválida"); }
}
