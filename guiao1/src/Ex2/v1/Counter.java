package Ex2.v1;

public class Counter {
    private int x;

    public Counter() {x = 0;}

    public synchronized void increment() {++x;}

    public int get() {return x;}
}
