package chat;

import rsa.Interpreter;
import rsa.RSADecryptor;
import rsa.RSAEncryptor;
import rsa.RSAGenerator;

import java.math.BigInteger;

public class User {
    private String name;
    private ChatSystem chatSystem;
    private BigInteger p;
    private BigInteger q;
    private BigInteger modulas;
    private BigInteger fi;
    private BigInteger encryptionKey;
    private BigInteger decryptionKey;

    public User(String name, ChatSystem chatSystem) {
        this.name = name;
        this.chatSystem = chatSystem;
        chatSystem.addUser(this);
        int counter = 0;
//        while (m.compareTo(modulas) == 1 && counter < 100) { removed this parasha ???
            p = RSAGenerator.generatePrimeNumber();
            q = RSAGenerator.generatePrimeNumber();
            modulas = RSAGenerator.getModulas(p, q);
            counter++;
//        }
        if (counter == 100) {
            System.out.println("Message is too big for current range of random numbers. Extend range of possible random generating values");
            return;
        }
        fi = RSAGenerator.getFi(p, q);
        encryptionKey = RSAGenerator.generateEncryptionKey(fi);
        decryptionKey = RSAGenerator.getDecryptionKey(encryptionKey, fi);
    }

    public void receiveMessage(BigInteger message) {
        System.out.println("User " + name + " just received message: " + message);
        BigInteger decryptedMessageNumber = RSADecryptor.decrypt(message, decryptionKey, modulas);
        System.out.println("Decrypted message number is " + decryptedMessageNumber);
        String decryptedMessage = Interpreter.interpretToWord(decryptedMessageNumber);
        System.out.println("Decrypted message is \"" + decryptedMessage + "\"");
    }

    public String getName() {
        return name;
    }

    public BigInteger getModulas() {
        return modulas;
    }

    public BigInteger getEncryptionKey() {
        return encryptionKey;
    }

    public BigInteger getSignature(BigInteger messageNumber) {
        return RSAEncryptor.encrypt(messageNumber, decryptionKey, modulas);
    }

    public void receiveMessageWithSignature(BigInteger encryptedMessage, BigInteger signature) {
        System.out.println("User " + name + " just received message WITH SIGNATURE: " + encryptedMessage);
        BigInteger decryptedMessageNumber = RSADecryptor.decrypt(encryptedMessage, decryptionKey, modulas);
        System.out.println("Decrypted message number is " + decryptedMessageNumber);
        String decryptedMessage = Interpreter.interpretToWord(decryptedMessageNumber);
        System.out.println("Decrypted message is \"" + decryptedMessage + "\"");
        User userFrom = chatSystem.getUsers().stream().filter(user -> {
            BigInteger encryptedSignature = RSADecryptor.decrypt(signature, user.getEncryptionKey(), user.getModulas());
            return encryptedSignature.equals(decryptedMessageNumber);
        }).findAny().orElseThrow(IllegalArgumentException::new);
        System.out.println("Message was sent from user: " + userFrom.getName());
    }
}
