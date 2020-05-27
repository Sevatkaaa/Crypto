package blockchain;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BlockChain {
    private static final AtomicInteger BLOCKS = new AtomicInteger();
    private static final int TRANSACTIONS_PER_BLOCK = 20;
    public static final String BLOCKS_DIR = "/Users/admin/Desktop/univ/BlockChain/src/main/resources/blockchain/";
    public static final String JSON_PREFIX = ".json";

    public static void main(String[] args) throws IOException {
        List<UserAccount> users = initUsers();
        createInitBlock();
        populateUsersWithMoney(users);
        for (int i = 0; i < 20; i++) {
            generateBlock(users);
        }
        boolean isWorking = true;
        while (isWorking) {
            isWorking = doAction(users);
        }
    }

    private static boolean doAction(List<UserAccount> users) throws IOException {
        System.out.println("\nChoose action:\n" +
                "1 - for block number X display all users and their money\n" +
                "2 - for every block display if it is valid\n" +
                "3 - balance for user X\n" +
                "4 - display all users and blocks that tx value is higher than X\n" +
                "5 - display all users and blocks that tx value is lower than X\n" +
                "Your input:");
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String action = r.readLine();
        if (action.equals("1")) {
            System.out.println("Enter block number(max: " + BLOCKS.get() + "): ");
            int blockNum = Integer.parseInt(r.readLine());
            List<Transaction> transactions = getBlocksTransactions(blockNum);
            Map<String, Integer> userMoney = new HashMap<>();
            List<String> userNames = users.stream().skip(1).map(UserAccount::getName).collect(Collectors.toList());
            userNames.forEach(user -> userMoney.put(user, 0));
            userNames.forEach(user -> {
                int minus = transactions.stream()
                        .filter(tx -> tx.getFrom().equals(user))
                        .map(Transaction::getMoney)
                        .mapToInt(Integer::intValue)
                        .sum();
                int plus = transactions.stream()
                        .filter(tx -> tx.getTo().equals(user))
                        .map(Transaction::getMoney)
                        .mapToInt(Integer::intValue)
                        .sum();
                int balance = plus - minus;
                System.out.println("Balance for user " + user + " is " + balance);
            });
        } else if (action.equals("2")) {
            List<Block> blocks = getBlocks(BLOCKS.get());
            blocks.stream().skip(1).forEach(block -> {
                String prevBlockHash = block.getPrevBlockHash();
                System.out.println("Prev block hash should be " + prevBlockHash);
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(BLOCKS_DIR + (block.getId() - 1) + JSON_PREFIX));
                    String blockInString = reader.readLine();
                    String hash = DigestUtils.sha256Hex(blockInString);
                    System.out.println("Real hash is " + hash);
                    if (hash.equals(prevBlockHash)) {
                        System.out.println("Block " + block.getId() + " is VALID");
                    } else {
                        System.out.println("Block " + block.getId() + " is NOT VALID");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else if (action.equals("3")) {
            System.out.println("Users: " + users.stream().skip(1).map(UserAccount::getName).collect(Collectors.joining(", ")));
            System.out.println("Enter user name: ");
            String userName = r.readLine();
            List<Transaction> transactions = getBlocksTransactions(BLOCKS.get());
            int minus = transactions.stream()
                    .filter(tx -> tx.getFrom().equals(userName))
                    .map(Transaction::getMoney)
                    .mapToInt(Integer::intValue)
                    .sum();
            int plus = transactions.stream()
                    .filter(tx -> tx.getTo().equals(userName))
                    .map(Transaction::getMoney)
                    .mapToInt(Integer::intValue)
                    .sum();
            int balance = plus - minus;
            System.out.println("Current balance for user " + userName + " is " + balance);
            System.out.println("Real balance for user " + userName + " is " + users.stream().filter(u -> u.getName().equals(userName)).findAny().get().getMoney());
        } else if (action.equals("4")) {
            System.out.println("Enter value: ");
            int value = Integer.parseInt(r.readLine());
            getBlocksTransactions(BLOCKS.get())
                    .stream()
                    .filter(tx -> tx.getMoney() > value)
                    .forEach(System.out::println);
        } else if (action.equals("5")) {
            System.out.println("Enter value: ");
            int value = Integer.parseInt(r.readLine());
            getBlocksTransactions(BLOCKS.get())
                    .stream()
                    .filter(tx -> tx.getMoney() < value)
                    .forEach(System.out::println);
        } else {
            System.out.println("bye");
            return false;
        }
        return true;
    }

    private static List<Block> getBlocks(int lastBlock) {
        List<Block> blocks = new ArrayList<>();
        for (int i = 0; i < lastBlock; i++) {
            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader(BLOCKS_DIR + i + JSON_PREFIX)) {
                Object obj = jsonParser.parse(reader);
                JSONObject blockJson = (JSONObject) obj;
                Block block = new Gson().fromJson(blockJson.toString(), Block.class);
                blocks.add(block);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        return blocks;
    }

    private static List<Transaction> getBlocksTransactions(int lastBlock) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < lastBlock; i++) {
            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader(BLOCKS_DIR + i + JSON_PREFIX)) {
                Object obj = jsonParser.parse(reader);
                JSONObject blockJson = (JSONObject) obj;
                Block block = new Gson().fromJson(blockJson.toString(), Block.class);
                transactions.addAll(block.getTransactions());
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        return transactions;
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
