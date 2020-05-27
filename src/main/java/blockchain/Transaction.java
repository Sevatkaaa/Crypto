package blockchain;

public class Transaction {
    private String from;
    private String to;
    private int money;

    public Transaction(String from, String to, int money) {
        this.from = from;
        this.to = to;
        this.money = money;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getMoney() {
        return money;
    }
}
