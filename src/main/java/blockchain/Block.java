package blockchain;


import java.util.List;

public class Block {
    private Integer id;
    private List<Transaction> transactions;
    private Integer prevBlockId;
    private String prevBlockHash;
    private Integer nonce;

    public Block(Integer id, Integer prevBlockId) {
        this.id = id;
        this.prevBlockId = prevBlockId;
    }

    public Integer getId() {
        return id;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Integer getPrevBlockId() {
        return prevBlockId;
    }

    public String getPrevBlockHash() {
        return prevBlockHash;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setPrevBlockHash(String prevBlockHash) {
        this.prevBlockHash = prevBlockHash;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public String computeHash() {

        return null;
    }

    private String convertToString() {
        return null;
    }
}
