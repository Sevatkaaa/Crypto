package blockchain;

public class BlockChain {
    public static final String BLOCKS_PATH = "/Users/admin/Desktop/univ/BlockChain/src/main/resources/blockchain/";

    private Block initBlock = null;

    public void setInitBlock(Block initBlock) {
        if (this.initBlock != null) {
            throw new IllegalArgumentException("Block chain is already initialized");
        }
        this.initBlock = initBlock;
    }
}
