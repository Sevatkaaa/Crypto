import java.math.BigInteger;

public class RSADecriptor {
    public static BigInteger decript(BigInteger encryptedMessage, BigInteger decriptionKey, BigInteger modulas) {
        String binaryD = decriptionKey.toString(2);
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
}
