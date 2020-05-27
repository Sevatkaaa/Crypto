package blockchain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockChain {
    private static final AtomicInteger BLOCKS = new AtomicInteger();
    private static final int TRANSACTIONS_PER_BLOCK = 20;

    public static void main(String[] args) {
        List<UserAccount> users = initUsers();
        createInitBlock();
        populateUsersWithMoney(users);
        for (int i = 0; i < 20; i++) {
            generateBlock(users);
        }
    }

    private static Block createInitBlock() {
        Block initBlock = new Block(0);
        initBlock.setTransactions(Collections.emptyList());
        initBlock.setPrevBlockHash(null);
        String hash = initBlock.computeHashAndSaveToFile();
        System.out.println("Initial block created with hash " + hash);
        BLOCKS.incrementAndGet();
        return initBlock;
    }

    private static void populateUsersWithMoney(List<UserAccount> users) {
        Block block = new Block(BLOCKS.get());
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 1; i < users.size(); i++) {
            Transaction tx = createInitTransaction(users.get(0), users.get(i));
            System.out.println("Just created initial tx: " + tx);
            transactions.add(tx);
        }
        block.setTransactions(transactions);
        String hash = block.computeHashAndSaveToFile();
        System.out.println("Initial block with money created with hash " + hash);
        BLOCKS.incrementAndGet();
    }

    private static Transaction createInitTransaction(UserAccount from, UserAccount to) {
        int money = (int) (Math.random() * 1000000);
        from.transfer(to, money);
        return new Transaction(from.getName(), to.getName(), money);
    }

    private static void generateBlock(List<UserAccount> users) {
        Block block = new Block(BLOCKS.get());
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < TRANSACTIONS_PER_BLOCK; i++) {
            Transaction tx = createRandomTransaction(users);
            System.out.println("Just created tx: " + tx);
            transactions.add(tx);
        }
        block.setTransactions(transactions);
        String hash = block.computeHashAndSaveToFile();
        System.out.println("Block with id " + block.getId() + " created with hash " + hash);
        BLOCKS.incrementAndGet();
    }

    private static Transaction createRandomTransaction(List<UserAccount> users) {
        int numOfUsers = users.size();
        Integer minUserMoney = users.stream().map(UserAccount::getMoney).min(Integer::compareTo).orElse(0);
        int from = (int) (Math.random() * numOfUsers);
        int to = (int) (Math.random() * numOfUsers);
        while (to == from) {
            to = (int) (Math.random() * numOfUsers);
        }
        int amount = (int) (Math.random() * minUserMoney) / numOfUsers;
        UserAccount fromUser = users.get(from);
        UserAccount toUser = users.get(to);
        fromUser.transfer(toUser, amount);
        return new Transaction(fromUser.getName(), toUser.getName(), amount);
    }

    private static List<UserAccount> initUsers() {
        List<UserAccount> users = new ArrayList<>();
        UserAccount user0 = new UserAccount("System", Integer.MAX_VALUE);
        UserAccount user1 = new UserAccount("Sov");
        UserAccount user2 = new UserAccount("Leo");
        UserAccount user3 = new UserAccount("Alina");
        UserAccount user4 = new UserAccount("Borys");
        UserAccount user5 = new UserAccount("Vol");
        UserAccount user6 = new UserAccount("Zuck");
        users.add(user0);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
        return users;
    }
}
