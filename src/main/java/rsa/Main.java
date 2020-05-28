package rsa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class Main {
    public static void main(String[] args) throws IOException {
       System.out.println("Enter a message:");
       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
       String message = reader.readLine();

       BigInteger m = Converter.convertToNumber(message);

       System.out.println(m);

       BigInteger p = BigInteger.ONE;
       BigInteger q = BigInteger.ONE;
       BigInteger n = BigInteger.ONE;
       while (m.compareTo(n) > 0) {
          p = RSAProvider.generatePrimeNumber();
          q = RSAProvider.generatePrimeNumber();
          n = RSAProvider.getN(p, q);
       }

       BigInteger fi = RSAProvider.getFi(p, q);

       BigInteger encryptionKey = RSAProvider.generateEncryptionKey(fi);
       BigInteger decryptionKey = RSAProvider.getDecryptionKey(encryptionKey, fi);
       BigInteger encryptedMessage = RSAProvider.decrypt(m, encryptionKey, n);
       BigInteger decryptedMessage = RSAProvider.decrypt(encryptedMessage, decryptionKey, n);
       String messageReturned = Converter.convertToWord(decryptedMessage);
       System.out.println(encryptedMessage);
       System.out.println(decryptedMessage);
       System.out.println(messageReturned);

       BigInteger signature = RSAProvider.decrypt(m, decryptionKey, n);
       System.out.println("Message with signature: (" + message + ", " + signature + ")");
    }
}
