package chat;

import java.math.BigInteger;

public class Chat {
    public static void main(String[] args) {
        ChatSystem chatSystem = new ChatSystem();
        User user1 = new User("Sov", chatSystem);
        User user2 = new User("Leo", chatSystem);
        User user3 = new User("Alina", chatSystem);
        User user4 = new User("Baguz", chatSystem);
        chatSystem.sendDirectMessage(user1, user2, "Hello");
        chatSystem.sendDirectMessageWithSignature(user1, user2, "Hello");
        chatSystem.sendMessageToEveryone(user3, "Hi Sov");
        chatSystem.sendMessageToEveryoneWithSignature(user3, "Hi Sov");
        chatSystem.sendMessageToEveryone(user3, "Hi Sov it's very interesting for me to communicate with you");

        Group group = new Group();
        group.addUser(user1);
        group.addUser(user2);
        group.addUser(user3);
        BigInteger groupMessage = group.createGroupMessage("Only group members can see this message");
        chatSystem.sendGroupMessageToEveryone(user2, groupMessage);
    }
}
