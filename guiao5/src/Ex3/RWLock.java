package Ex3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RWLock {
    private ReentrantLock lock;
    private Condition waitRead;
    private Condition waitWrite;
    private int readers;
    private int writer;

    public RWLock() {
        lock = new ReentrantLock();
        waitRead = lock.newCondition();
        waitWrite = lock.newCondition();
        readers = 0;
        writer = 0;
    }

    public void readLock() {
        lock.lock();
        while (writer > 0)
            try {waitWrite.await();} catch (InterruptedException e) {e.printStackTrace();}
        ++readers;
        lock.unlock();
    }

    public void readUnlock() {
        lock.lock();
        --readers;
        if (readers == 0)
            waitWrite.signal();
        lock.unlock();
    }

    public void writeLock() {
        lock.lock();
        while (writer > 0 || readers > 0)
            try {waitWrite.await();} catch (InterruptedException e) {e.printStackTrace();}
        writer = 1;
        lock.unlock();
    }

    public void writeUnlock() {
        lock.lock();
        writer = 0;
        waitWrite.signal();
        waitRead.signalAll();
        lock.unlock();
    }
}
