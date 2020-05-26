package chat;

public class Chat {
    public static void main(String[] args) {
        ChatSystem chatSystem = new ChatSystem();
        User user1 = new User("Sov", chatSystem);
        User user2 = new User("Leo", chatSystem);
        User user3 = new User("Alina", chatSystem);
        chatSystem.sendDirectMessage(user1, user2, "Hello");
        chatSystem.sendDirectMessageWithSignature(user1, user2, "Hello");
        chatSystem.sendMessageToEveryone(user3, "Hi Sov");
    }
}
