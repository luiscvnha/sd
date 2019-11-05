package Ex2;

import java.util.HashMap;

public class Warehouse {
    private HashMap<String, Item> stock;

    public Warehouse() { stock = new HashMap<>(); }

    public void supply(String item, int quantity) { stock.get(item).supply(quantity); }

    public void consume(String[] items) {
        for (String s: items)
            stock.get(s).consume();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Warehouse {");

        Iterator<Map.Entry<String, Item>> itr = stock.entrySet().iterator();
        if (itr.hasNext()) {
            Map.Entry<String, Item> entry = itr.next();
            sb.append(entry.getKey()).append(" ").append(entry.getValue().getQuantity());
            while (itr.hasNext()) {
                entry = itr.next();
                sb.append(", ").append(entry.getKey()).append(" ").append(entry.getValue().getQuantity());
            }
        }

        sb.append("}");
        return sb.toString();
    }
}
