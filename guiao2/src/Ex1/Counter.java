package Ex1;

public class Counter {
    private int x;

    public Counter() {x = 0;}

    public synchronized void increment() {++x;}

    public synchronized int get() {return x;}
}
