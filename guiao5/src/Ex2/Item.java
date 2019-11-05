package Ex2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Item {
    private ReentrantLock lock;
    private Condition isEmpty;
    private int quantity;

    public Item() {
        lock = new ReentrantLock();
        isEmpty = lock.newCondition();
        quantity = 0;
    }

    public int getQuantity() {
        lock.lock();
        int r = quantity;
        lock.unlock();
        return r;
    }

    public void supply(int quantity) {
        lock.lock();
        this.quantity += quantity;
        isEmpty.signalAll();
        lock.unlock();
    }

    public void consume() {
        lock.lock();
        while (quantity <= 0) {
            try {isEmpty.await();} catch (InterruptedException e) {e.printStackTrace();}
        }
        --quantity;
        lock.unlock();
    }
}
