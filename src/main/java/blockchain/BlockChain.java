package blockchain;

public class BlockChain {
    public static final String BLOCKS_PATH = "/Users/admin/Desktop/univ/CryptaLab1/src/resources";

    private Block initBlock;

    public void setInitBlock(Block initBlock) {
        if (initBlock != null) {
            throw new IllegalArgumentException("Block chain is already initialized");
        }
        this.initBlock = initBlock;
    }
}
