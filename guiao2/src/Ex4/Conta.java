public class Conta {
    private double valor;

    public Conta() {
        this.valor = 0.0;
    }

    public synchronized double get() { return this.valor; }

    public synchronized void add(double x) { this.valor += x; }

    public synchronized void sub(double x) { this.valor -= x; }
}
