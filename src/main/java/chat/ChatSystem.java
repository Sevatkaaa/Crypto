package chat;

import rsa.Converter;
import rsa.RSAProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ChatSystem {
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public void sendDirectMessage(User fromUser, User toUser, String message) {
        sendMessage(fromUser, message, toUser);
    }

    public void sendMessageToEveryone(User fromUser, String message) {
        System.out.println("\nUser " + fromUser.getName() + " sends message \"" + message + "\" to everyone\n");
        users.forEach(user -> sendMessage(fromUser, message, user));
    }

    public void sendGroupMessageToEveryone(User fromUser, BigInteger message) {
        System.out.println("\nUser " + fromUser.getName() + " sends GROUP message \"" + message + "\" to everyone\n");
        users.forEach(user -> user.receiveMessage(message));
    }

    private void sendMessage(User fromUser, String message, User user) {
        System.out.println("User " + fromUser.getName() + " sends message \"" + message + "\" to user " + user.getName());
        BigInteger encryptionKey = user.getEncryptionKey();
        BigInteger n = user.getN();
        BigInteger messageNumber = Converter.convertToNumber(message);
        BigInteger encryptedMessage = RSAProvider.decrypt(messageNumber, encryptionKey, n);
        user.receiveMessage(encryptedMessage);
    }

    public void sendDirectMessageWithSignature(User fromUser, User toUser, String message) {
        sendMessageWithSignature(fromUser, toUser, message);
    }

    private void sendMessageWithSignature(User fromUser, User toUser, String message) {
        System.out.println("\nUser " + fromUser.getName() + " sends message \"" + message + "\" WITH SIGNATURE to user " + toUser.getName());
        BigInteger encryptionKey = toUser.getEncryptionKey();
        BigInteger n = toUser.getN();
        BigInteger messageNumber = Converter.convertToNumber(message);
        BigInteger encryptedMessage = RSAProvider.decrypt(messageNumber, encryptionKey, n);
        BigInteger signature = fromUser.getSignature(messageNumber);
        toUser.receiveMessageWithSignature(encryptedMessage, signature);
    }

    public void sendMessageToEveryoneWithSignature(User fromUser, String message) {
        System.out.println("\nUser " + fromUser.getName() + " sends message \"" + message + "\" WITH SIGNATURE to everyone\n");
        users.forEach(user -> sendMessageWithSignature(fromUser, user, message));
    }

    public List<User> getUsers() {
        return users;
    }
}
