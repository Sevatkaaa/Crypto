package chat;

import rsa.Interpreter;
import rsa.RSAEncryptor;

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

    private void sendMessage(User fromUser, String message, User user) {
        System.out.println("User " + fromUser.getName() + " sends message \"" + message + "\" to user " + user.getName());
        BigInteger encryptionKey = user.getEncryptionKey();
        BigInteger modulas = user.getModulas();
        BigInteger messageNumber = Interpreter.interpretToNumber(message);
        BigInteger encryptedMessage = RSAEncryptor.encrypt(messageNumber, encryptionKey, modulas);
        user.receiveMessage(encryptedMessage);
    }
}
