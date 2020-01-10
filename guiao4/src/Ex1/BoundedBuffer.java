package Ex1;

public class BoundedBuffer {
    private int[] array;
    private int poswrite;

    public BoundedBuffer(int size) {
        array = new int[size];
        poswrite = 0;
    }

    public synchronized void put(int v) {
        while (poswrite >= array.length) {
            try {wait();} catch (InterruptedException ignored) {}
        }
        array[poswrite] = v;
        ++poswrite;
        notifyAll();
    }

    public synchronized int get() {
        while (poswrite <= 0) {
            try {wait();} catch (InterruptedException ignored) {}
        }
        int r = array[--poswrite];
        notifyAll();
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
