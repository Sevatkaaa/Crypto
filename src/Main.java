import java.io.IOException;
import java.util.Random;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static <bool> void main(String[] args) throws IOException {
       System.out.println("Enter a message:");
       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
       String message = reader.readLine();

       BigInteger m = WordToNumberInterpretator.interpretateToNumber(message);

       System.out.println(m);

       BigInteger p = BigInteger.ONE;
       BigInteger q = BigInteger.ONE;
       BigInteger modulas = BigInteger.ONE;
       int counter = 0;
       while (m.compareTo(modulas) == 1 && counter < 100) {
          p = RSAGenerator.generatePrimeNumber();
          q = RSAGenerator.generatePrimeNumber();
          modulas = RSAGenerator.getModulas(p, q);
          counter++;
       }

       if (counter == 100) {
          System.out.println("Message is too big for current range of random numbers. Extend range of possible random generating values");
          return;
       }

       BigInteger fi = RSAGenerator.getFi(p, q);

       BigInteger encriptionKey = RSAGenerator.generateEncriptionKey(fi);

       BigInteger decriptionKey = RSAGenerator.getDecriptionKey(encriptionKey, fi);

       BigInteger encriptedMessage = RSAEncriptor.encript(m, encriptionKey, modulas);

       BigInteger decriptedMessage = RSADecriptor.decript(encriptedMessage, decriptionKey, modulas);

       String messageReturned = WordToNumberInterpretator.interpretateToWord(decriptedMessage);

       System.out.println(encriptedMessage);
       System.out.println(decriptedMessage);
       System.out.println(messageReturned);

       BigInteger signature = RSAEncriptor.encript(m, decriptionKey, modulas);
       System.out.println("Message with signature: (" + message + ", " + signature + ")");
    }
}
