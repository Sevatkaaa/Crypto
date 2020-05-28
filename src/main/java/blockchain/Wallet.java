package blockchain;

public class Wallet {
    private String name;
    private int money;

    public Wallet(String name) {
        this(name, 0);
    }

    public Wallet(String name, int money) {
        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public void transfer(Wallet to, int money) {
        this.money -= money;
        to.money += money;
    }
}
