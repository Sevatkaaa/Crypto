package chat;

import rsa.Converter;
import rsa.RSAProvider;

import java.math.BigInteger;
import java.util.Objects;

public class User {
    private String name;
    private ChatSystem chatSystem;
    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger fi;
    private BigInteger encryptionKey;
    private BigInteger decryptionKey;

    public User(String name, ChatSystem chatSystem) {
        this.name = name;
        this.chatSystem = chatSystem;
        chatSystem.addUser(this);
        p = RSAProvider.generatePrimeNumber();
        q = RSAProvider.generatePrimeNumber();
        n = RSAProvider.getN(p, q);
        fi = RSAProvider.getFi(p, q);
        encryptionKey = RSAProvider.generateEncryptionKey(fi);
        decryptionKey = RSAProvider.getDecryptionKey(encryptionKey, fi);
    }

    public void receiveMessage(BigInteger message) {
        System.out.println("\nUser " + name + " just received message: " + message);
        BigInteger decryptedMessageNumber = RSAProvider.decrypt(message, decryptionKey, n);
        System.out.println("Decrypted message number is " + decryptedMessageNumber);
        String decryptedMessage = Converter.convertToWord(decryptedMessageNumber);
        System.out.println("Decrypted message is \"" + decryptedMessage + "\"");
    }

    public String getName() {
        return name;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getEncryptionKey() {
        return encryptionKey;
    }

    public BigInteger getSignature(BigInteger messageNumber) {
        return RSAProvider.decrypt(messageNumber, decryptionKey, n);
    }

    public void receiveMessageWithSignature(BigInteger encryptedMessage, BigInteger signature) {
        System.out.println("User " + name + " just received message WITH SIGNATURE: " + encryptedMessage);
        BigInteger decryptedMessageNumber = RSAProvider.decrypt(encryptedMessage, decryptionKey, n);
        System.out.println("Decrypted message number is " + decryptedMessageNumber);
        String decryptedMessage = Converter.convertToWord(decryptedMessageNumber);
        System.out.println("Decrypted message is \"" + decryptedMessage + "\"");
        User userFrom = chatSystem.getUsers().stream().filter(user -> {
            BigInteger encryptedSignature = RSAProvider.decrypt(signature, user.getEncryptionKey(), user.getN());
            return encryptedSignature.equals(decryptedMessageNumber);
        }).findAny().orElseThrow(IllegalArgumentException::new);
        System.out.println("Message was sent from user: " + userFrom.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
