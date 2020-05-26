import java.math.BigInteger;

public class RSAEncryptor {
    public static BigInteger encrypt(BigInteger message, BigInteger encryptionKey, BigInteger modulas) {
        String binaryE = encryptionKey.toString(2);
        BigInteger c = BigInteger.ONE;
        BigInteger temp;
        for (int i = binaryE.length() - 1; i >= 0; i--) {
            if (i == binaryE.length() - 1) {
                temp = message.mod(modulas);
            } else {
                temp = message.pow(2).mod(modulas);
                message = temp;
            }
            if (binaryE.charAt(i) == '1') {
                c = c.multiply(temp).mod(modulas);
            }

        }
        return c;
    }
}
