package blockchain;

public class UserAccount {
    private String name;
    private int money;

    public UserAccount(String name) {
        this.name = name;
        this.money = 0;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }
}
