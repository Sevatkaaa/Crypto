import java.math.BigInteger;

public class BigIntegerExtension {
     public static BigInteger modPow(BigInteger number, BigInteger degree, BigInteger modulas) {
        BigInteger res = BigInteger.ONE;
        for (BigInteger i = BigInteger.ONE; i.compareTo(degree) == -1; i = i.add(BigInteger.ONE)) {
            res = res.multiply(res);
            res = res.mod(modulas);
        }
        return res;
     }
}
