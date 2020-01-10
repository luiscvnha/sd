package Ex2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Warehouse {
    private Map<String, Item> stock;
    private Lock lockStock;
    private Condition isEmpty;

    public Warehouse() {
        stock = new HashMap<>();
        lockStock = new ReentrantLock();
        isEmpty = lockStock.newCondition();
    }

    public void supply(String itemName, int quantity) {
        if (quantity <= 0)
            return;

        lockStock.lock();

        if (!stock.containsKey(itemName)) {
            stock.put(itemName, new Item(quantity));
            lockStock.unlock();
        } else {
            Item item = stock.get(itemName);

            item.lock();
            lockStock.unlock();

            item.supply(quantity);

            isEmpty.signalAll();
            item.unlock();
        }
    }

    public void consume(String[] itemNames) {
        Item[] items = new Item[itemNames.length];
        int length = 0;

        lockStock.lock();

        Item aux;
        for (String itemName : itemNames) {
            if ((aux = stock.get(itemName)) != null) {
                items[length++] = aux;
                aux.lock();
            }
        }

        lockStock.unlock();

        for (Item item : items) {
            while (item.getQuantity() <= 0) {
                try {isEmpty.await();} catch (InterruptedException ignored) {}
            }

            item.consume();
            item.unlock();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Warehouse {");

        Iterator<Map.Entry<String, Item>> itr = stock.entrySet().iterator();
        if (itr.hasNext()) {
            Map.Entry<String, Item> entry = itr.next();
            sb.append(entry.getKey()).append(": ").append(entry.getValue().getQuantity());
            while (itr.hasNext()) {
                entry = itr.next();
                sb.append(", ").append(entry.getKey()).append(": ").append(entry.getValue().getQuantity());
            }
        }

        sb.append("}");
        return sb.toString();
    }
}
