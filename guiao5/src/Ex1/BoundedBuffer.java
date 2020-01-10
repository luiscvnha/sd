package Ex1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class BoundedBuffer {
    private int[] array;
    private int poswrite;
    private Lock lock;
    private Condition full;
    private Condition empty;

    public BoundedBuffer(int s) {
        array = new int[s];
        poswrite = 0;
        lock = new ReentrantLock();
        full = lock.newCondition();
        empty = lock.newCondition();
    }

    public void put(int v) {
        lock.lock();

        while (poswrite >= array.length) {
            try {full.await();} catch (InterruptedException ignored) {}
        }

        array[poswrite++] = v;

        empty.signal();

        lock.unlock();
    }

    public int get() {
        lock.lock();

        while (poswrite <= 0) {
            try {empty.await();} catch (InterruptedException ignored) {}
        }

        int r = array[--poswrite];

        full.signal();

        lock.unlock();

        return r;
    }

    public String toString() {
        if (poswrite <= 0) {
            return "[]";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            int i;
            for (i = 0; i < poswrite-1; ++i)
                sb.append(array[i]).append(", ");
            sb.append(array[i]);
            sb.append("]");
            return sb.toString();
        }
    }
}
