package blockchain;


import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Block {
    private static final String HASH_PREFIX = "000";
    private static final String JSON_SUFFIX = ".json";

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

    public String computeHashAndSaveToFile() {
        JSONObject block = getBasicBlockJSON();
        int nonce = 0;
        while (true) {
            block.put("nonce", ++nonce);
            String blockString = block.toJSONString();
            String hash = DigestUtils.sha256Hex(blockString);
            if (hash.startsWith(HASH_PREFIX)) {
                this.setNonce(nonce);
                saveToFile(blockString);
                return hash;
            }
        }
    }

    private void saveToFile(String blockString) {
        try (FileWriter file = new FileWriter(BlockChain.BLOCKS_PATH + id + JSON_SUFFIX)) {
            file.write(blockString);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getBasicBlockJSON() {
        JSONObject block = new JSONObject();
        block.put("id", id);
        JSONArray transactions = new JSONArray();
        this.transactions.stream().forEach(tx -> {
            JSONObject transaction = new JSONObject();
            transaction.put("from", tx.getFrom());
            transaction.put("to", tx.getTo());
            transaction.put("money", tx.getMoney());
            transactions.add(transaction);
        });
        block.put("transactions", transactions);
        block.put("prevBlockId", id - 1);
        block.put("prevBlockHash", "hash"); // refactor later to getHashFromBlockMethod
        return block;
    }

    private String convertToString() {
        return null;
    }
}
