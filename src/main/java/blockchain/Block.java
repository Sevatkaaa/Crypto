package blockchain;


import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.List;

public class Block {
    private static final String BLOCKS_PATH = "/Users/admin/Desktop/univ/BlockChain/src/main/resources/blockchain/";
    private static final String HASH_PREFIX = "000";
    private static final String JSON_SUFFIX = ".json";

    private Integer id;
    private List<Transaction> transactions;
    private Integer prevBlockId;
    private String prevBlockHash;
    private Integer nonce;

    public Block(Integer id) {
        this.id = id;
        this.prevBlockId = id - 1;
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
        try (FileWriter file = new FileWriter(BLOCKS_PATH + id + JSON_SUFFIX)) {
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
        block.put("prevBlockHash", id == 0 ? "hash" : getHashForPrevBlock());
        return block;
    }

    private String getHashForPrevBlock() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BLOCKS_PATH + (id - 1) + JSON_SUFFIX))) {
            String prevFile = reader.readLine();
            return DigestUtils.sha256Hex(prevFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
