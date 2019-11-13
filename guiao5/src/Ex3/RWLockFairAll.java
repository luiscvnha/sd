package Ex3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RWLockFairAll {
    private ReentrantLock lock;
    private Condition readersCond;
    private Condition writersCond;
    private int readers;
    private int writer;
    private int readRequests;
    private int writeRequests;
    private int readersPriority;
    private int writersPriority;
    private final int MAXPRIORITY = 3;

    public RWLockFairAll() {
        lock = new ReentrantLock();
        readersCond = lock.newCondition();
        writersCond = lock.newCondition();
        readers = writer = 0;
        readRequests = writeRequests = 0;
        readersPriority = writersPriority = 0;
    }

    public void readLock() {
        lock.lock();
        ++readRequests;
        while (writer > 0 || (readersPriority >= MAXPRIORITY && writeRequests > 0))
            try {readersCond.await();} catch (InterruptedException ignored) {}
        --readRequests;
        ++readers;
        ++readersPriority;
        if (readersPriority == MAXPRIORITY)
            writersPriority = 0;
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
        while (writer > 0 || readers > 0 || (writersPriority >= MAXPRIORITY && readRequests > 0))
            try {writersCond.await();} catch (InterruptedException ignored) {}
        --writeRequests;
        writer = 1;
        ++writersPriority;
        if (writersPriority == MAXPRIORITY)
            readersPriority = 0;
        lock.unlock();
    }

    public void writeUnlock() {
        lock.lock();
        writer = 0;
        readersCond.signalAll();
        writersCond.signal();
        lock.unlock();
    }
}
