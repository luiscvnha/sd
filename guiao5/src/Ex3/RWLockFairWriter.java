package Ex3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RWLockFairWriter implements RWLock {
    private ReentrantLock lock;
    private Condition readersCond;
    private Condition writersCond;
    private int readers;
    private int writer;
    private int writeRequests;

    public RWLockFairWriter() {
        lock = new ReentrantLock();
        readersCond = lock.newCondition();
        writersCond = lock.newCondition();
        readers = writer = 0;
        writeRequests = 0;
    }

    public void readLock() {
        lock.lock();
        while (writer > 0 || writeRequests > 0)
            try {readersCond.await();} catch (InterruptedException ignored) {}
        ++readers;
        lock.unlock();
    }

    public void readUnlock() {
        lock.lock();
        --readers;
        if (readers == 0)
            writersCond.signal();
        lock.unlock();
    }

    public void writeLock() {
        lock.lock();
        ++writeRequests;
        while (writer > 0 || readers > 0)
            try {writersCond.await();} catch (InterruptedException ignored) {}
        --writeRequests;
        writer = 1;
        lock.unlock();
    }

    public void writeUnlock() {
        lock.lock();
        writer = 0;
        writersCond.signal();
        readersCond.signalAll();
        lock.unlock();
    }
}
