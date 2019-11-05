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
        stock.forEach( (s, i) -> sb.append(s).append(" ").append(i.getQuantity()).append(", "));
        if (!stock.isEmpty()) {
            int len = sb.length();
            sb.delete(len - 2, len);
        }
        sb.append("}");
        return sb.toString();
    }
}
