package blockchain;

public class UserAccount {
    private String name;
    private int money;

    public UserAccount(String name) {
        this(name, 0);
    }

    public UserAccount(String name, int money) {
        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public void transfer(UserAccount to, int money) {
        this.money -= money;
        to.money += money;
    }
}
