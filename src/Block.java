import java.util.Arrays;
import java.util.Objects;

public class Block {
    private String transactions;
    private int blockHash;
    private int previousBlockHash;

    public Block(String transactions, int previousHashCode) {
        this.transactions = transactions;
        this.previousBlockHash = previousHashCode;
        this.blockHash = calculateHash();
    }

    public String getTransactions() {
        return transactions;
    }

    public int getHash() {
        return blockHash;
    }

    public int getPreviousBlockHash() {
        return previousBlockHash;
    }

    public int calculateHash() {
        return Objects.hash(transactions, previousBlockHash);
    }

    @Override
    public String toString() {
        return "Block{" +
                "transactions='" + transactions + '\'' +
                ", blockHash=" + blockHash +
                ", previousBlockHash=" + previousBlockHash +
                '}';
    }
}