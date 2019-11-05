package Ex4;

public class Conta {
    private double valor;

    public Conta() { valor = 0.0; }

    public synchronized double get() { return valor; }

    public synchronized void add(double x) { valor += x; }

    public synchronized void sub(double x) { valor -= x; }
}
