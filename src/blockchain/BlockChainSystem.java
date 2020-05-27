package blockchain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockChainSystem {
    public static void main(String[] args) {
        BlockChain blockChain = new BlockChain();
        List<UserAccount> users = initUsers();
        Block initBlock = createInitBlock();
        blockChain.setInitBlock(initBlock);
        while (true) {
            generateBlock(blockChain, users);
        }
    }

    private static Block createInitBlock() {
        Block initBlock = new Block(0, null);
        initBlock.setTransactions(Collections.emptyList());
        initBlock.setPrevBlockHash(null);
        initBlock.setNonce(0);
        String hash = initBlock.computeHash();
        return initBlock;
    }

    private static void generateBlock(BlockChain blockChain, List<UserAccount> users) {

    }

    private static List<UserAccount> initUsers() {
        List<UserAccount> users = new ArrayList<>();
        UserAccount user0 = new UserAccount("SystemInit");
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
