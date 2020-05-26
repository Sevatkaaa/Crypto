package rsa;

import java.math.BigInteger;

public class RSAProvider {
    public static BigInteger generatePrimeNumber() {
        return RandomPrimeNumberGenerator.generate(new BigInteger("3"), new BigInteger("100000000000000000000000000000001000000000000000000000000000000010000000000000000000000000000000100000000000000000000000000000001000000000000000000000000000000010000000000000000000000000000000"));
    }

    public static BigInteger getModulas(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }

    public static BigInteger getFi(BigInteger p, BigInteger q) {
        return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    }

    public static BigInteger generateEncryptionKey(BigInteger n) {
        if (n.compareTo(new BigInteger("5000")) == -1) {
            BigInteger encriptionKey = BigInteger.ONE;
            while (n.mod(encriptionKey) == BigInteger.ZERO) {
                encriptionKey = RandomPrimeNumberGenerator.generate(new BigInteger("2"), n);
            }
            return encriptionKey;
        }
        else {
            BigInteger encriptionKey = BigInteger.ONE;
            while (n.mod(encriptionKey) == BigInteger.ZERO) {
                encriptionKey = RandomPrimeNumberGenerator.generate(new BigInteger("2"), new BigInteger("500"));
            }
            return encriptionKey;
        }
    }

    public static BigInteger decrypt(BigInteger encryptedMessage, BigInteger decryptionKey, BigInteger modulas) {
        String binaryD = decryptionKey.toString(2);
        BigInteger message = BigInteger.ONE;
        BigInteger temp;
        for (int i = binaryD.length() - 1; i >= 0; i--) {
            if (i == binaryD.length() - 1) {
                temp = encryptedMessage.mod(modulas);
            } else {
                temp = encryptedMessage.pow(2).mod(modulas);
                encryptedMessage = temp;
            }
            if (binaryD.charAt(i) == '1') {
                message = message.multiply(temp).mod(modulas);
            }
        }
        return message;
    }

    public static BigInteger getDecryptionKey(BigInteger e, BigInteger fi) {
        return e.modInverse(fi);
    }
}
