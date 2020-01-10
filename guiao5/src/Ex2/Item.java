package Ex2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Item {
    private int quantity;
    private Lock lock = new ReentrantLock();

    public Item() {
        quantity = 0;
    }

    public Item(int initialQuantity) {
        quantity = initialQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void supply(int quantity) {
        this.quantity += quantity;
    }

    public void consume() {
        --quantity;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }
}
