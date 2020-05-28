package blockchain;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BlockChain {
    private static int NOW_BLOCKS = 0;
    private static final int TRANSACTIONS_PER_BLOCK = 20;
    private static final String BLOCKS_DIR = "/Users/admin/Desktop/univ/COVID-19/crypto/BlockChain/src/main/resources/blockchain/";
    private static final String JSON_PREFIX = ".json";
    private static final int BLOCKS_TOTAL = 20;

    public static void main(String[] args) throws IOException {
        List<UserAccount> users = new ArrayList<>();
        users.add(new UserAccount("System", Integer.MAX_VALUE));
        users.add(new UserAccount("Sov"));
        users.add(new UserAccount("Leo"));
        users.add(new UserAccount("Alina"));
        users.add(new UserAccount("Borys"));
        users.add(new UserAccount("Vol"));
        users.add(new UserAccount("Zuck"));
        initializeBlockChain(users);
        for (int i = 0; i < BLOCKS_TOTAL; i++) {
            generateBlock(users);
        }
        boolean doAction = true;
        while (doAction) {
            doAction = doAction(users);
        }
    }

    private static boolean doAction(List<UserAccount> users) throws IOException {
        System.out.println("\nPrint action number:\n" +
                "1 - display all users and their money before block\n" +
                "2 - check blocks for corruption\n" +
                "3 - user money\n" +
                "4 - transactions that transaction value is higher than X and lower than Y\n" +
                "Make a choice:");
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String action = r.readLine();
        if (action.equals("1")) {
            System.out.println("Block num: ");
            int blockNum = Integer.parseInt(r.readLine());
            List<Transaction> transactions = getBlocksTransactions(blockNum);
            List<String> userNames = users.stream().skip(1).map(UserAccount::getName).collect(Collectors.toList());
            userNames.forEach(user -> {
                int minus = getTxsFrom(transactions, user);
                int plus = getTxsTo(transactions, user);
                int balance = plus - minus;
                System.out.println("User " + user + " has money " + balance);
            });
        } else if (action.equals("2")) {
            List<Block> blocks = getBlocks(NOW_BLOCKS);
            blocks.stream().skip(1).forEach(block -> {
                String prevBlockHash = block.getPrevBlockHash();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(BLOCKS_DIR + (block.getId() - 1) + JSON_PREFIX));
                    String blockInString = reader.readLine();
                    String hash = DigestUtils.sha256Hex(blockInString);
                    if (!hash.equals(prevBlockHash)) {
                        System.out.println("Block " + block.getId() + " is corrupted, all other block after it may have incorrect data");
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("All blocks have correct data, all hashes are equal to expected values");
        } else if (action.equals("3")) {
            System.out.println("User list: " + users.stream().skip(1).map(UserAccount::getName).collect(Collectors.joining(", ")));
            System.out.println("User: ");
            String userName = r.readLine();
            List<Transaction> transactions = getBlocksTransactions(NOW_BLOCKS);
            int balance = getTxsTo(transactions, userName) - getTxsFrom(transactions, userName);
            System.out.println("Current balance for user " + userName + " is " + balance);
        } else if (action.equals("4")) {
            System.out.println("X (lower bound): ");
            int value = Integer.parseInt(r.readLine());
            System.out.println("Y (upper bound): ");
            int value2 = Integer.parseInt(r.readLine());
            getBlocksTransactions(NOW_BLOCKS)
                    .stream()
                    .filter(tx -> tx.getMoney() > value && tx.getMoney() < value2)
                    .forEach(System.out::println);
        } else {
            System.out.println("bye");
            return false;
        }
        return true;
    }

    private static int getTxsTo(List<Transaction> transactions, String userName) {
        return transactions.stream()
                .filter(tx -> tx.getTo().equals(userName))
                .map(Transaction::getMoney)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private static int getTxsFrom(List<Transaction> transactions, String user) {
        return transactions.stream()
                .filter(tx -> tx.getFrom().equals(user))
                .map(Transaction::getMoney)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private static List<Block> getBlocks(int lastBlock) {
        List<Block> blocks = new ArrayList<>();
        for (int i = 0; i < lastBlock; i++) {
            try (FileReader reader = new FileReader(BLOCKS_DIR + i + JSON_PREFIX)) {
                blocks.add(getParsedBlock(reader));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return blocks;
    }

    private static List<Transaction> getBlocksTransactions(int lastBlock) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < lastBlock; i++) {
            try (FileReader reader = new FileReader(BLOCKS_DIR + i + JSON_PREFIX)) {
                transactions.addAll(getParsedBlock(reader).getTransactions());
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        return transactions;
    }

    private static Block getParsedBlock(FileReader reader) throws IOException, ParseException {
        Gson GSON = new Gson();
        JSONParser JSON_PARSER = new JSONParser();
        return GSON.fromJson((JSON_PARSER.parse(reader)).toString(), Block.class);
    }

    private static Block initializeBlockChain(List<UserAccount> users) {
        Block initBlock = new Block(0);
        initBlock.setTransactions(Collections.emptyList());
        initBlock.setPrevBlockHash(null);
        String hash = initBlock.computeHashAndSaveToFile();
        System.out.println("Initial block created with hash " + hash);
        NOW_BLOCKS++;
        sendInitialMoney(users);
        return initBlock;
    }

    private static void sendInitialMoney(List<UserAccount> users) {
        Block block = new Block(NOW_BLOCKS);
        List<Transaction> transactions = users.stream()
                .skip(1)
                .map(user -> {
                    Transaction tx = createInitTransaction(users.get(0), user);
                    System.out.println("Just created initial tx: " + tx);
                    return tx;
                })
                .collect(Collectors.toList());
        block.setTransactions(transactions);
        String hash = block.computeHashAndSaveToFile();
        System.out.println("Initial block with money created with hash " + hash);
        NOW_BLOCKS++;
    }

    private static Transaction createInitTransaction(UserAccount from, UserAccount to) {
        int money = (int) (Math.random() * 1000000);
        from.transfer(to, money);
        return new Transaction(from.getName(), to.getName(), money);
    }

    private static void generateBlock(List<UserAccount> users) {
        Block block = new Block(NOW_BLOCKS++);
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < TRANSACTIONS_PER_BLOCK; i++) {
            Transaction tx = createRandomTransaction(users);
            System.out.println("Just created tx: " + tx);
            transactions.add(tx);
        }
        block.setTransactions(transactions);
        String hash = block.computeHashAndSaveToFile();
        System.out.println("Block with id " + block.getId() + " created with hash " + hash);
    }

    private static Transaction createRandomTransaction(List<UserAccount> users) {
        int numOfUsers = users.size();
        Integer minUserMoney = users.stream()
                .map(UserAccount::getMoney)
                .min(Integer::compareTo)
                .orElse(0);
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

}
