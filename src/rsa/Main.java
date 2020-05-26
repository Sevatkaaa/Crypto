package rsa;

import java.io.IOException;
import java.math.BigInteger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
       System.out.println("Enter a message:");
       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
       String message = reader.readLine();

       BigInteger m = Converter.convertToNumber(message);

       System.out.println(m);

       BigInteger p = BigInteger.ONE;
       BigInteger q = BigInteger.ONE;
       BigInteger modulas = BigInteger.ONE;
       int counter = 0;
       while (m.compareTo(modulas) == 1 && counter < 100) {
          p = RSAProvider.generatePrimeNumber();
          q = RSAProvider.generatePrimeNumber();
          modulas = RSAProvider.getModulas(p, q);
          counter++;
       }

       if (counter == 100) {
          System.out.println("Message is too big for current range of random numbers. Extend range of possible random generating values");
          return;
       }

       BigInteger fi = RSAProvider.getFi(p, q);

       BigInteger encryptionKey = RSAProvider.generateEncryptionKey(fi);

       BigInteger decryptionKey = RSAProvider.getDecryptionKey(encryptionKey, fi);

       BigInteger encryptedMessage = RSAProvider.decrypt(m, encryptionKey, modulas);

       BigInteger decryptedMessage = RSAProvider.decrypt(encryptedMessage, decryptionKey, modulas);

       String messageReturned = Converter.convertToWord(decryptedMessage);

       System.out.println(encryptedMessage);
       System.out.println(decryptedMessage);
       System.out.println(messageReturned);

       BigInteger signature = RSAProvider.decrypt(m, decryptionKey, modulas);
       System.out.println("Message with signature: (" + message + ", " + signature + ")");
    }
}
