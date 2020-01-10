package Ex3;

public interface RWLock {
    void readLock();

    void readUnlock();

    void writeLock();

    void writeUnlock();
}
