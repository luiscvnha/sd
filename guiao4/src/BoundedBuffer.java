public class BoundedBuffer {
    private int[] array;
    private int poswrite;

    public BoundedBuffer(int s) {
        array = new int[s];
        poswrite = 0;
    }

    public synchronized void put(int v) {
        while (poswrite >= array.length) {
            try {wait();}
            catch (InterruptedException e) {e.printStackTrace();}
        }
        array[poswrite] = v;
        ++poswrite;
        notifyAll();
    }

    public synchronized int get() {
        while (poswrite <= 0) {
            try {wait();}
            catch (InterruptedException e) {e.printStackTrace();}
        }
        int r = array[--poswrite];
        notifyAll();
        return r;
    }

    @Override
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
