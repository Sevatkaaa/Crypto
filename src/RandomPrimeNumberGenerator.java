import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomPrimeNumberGenerator {
    public static BigInteger genetate(BigInteger minLimit, BigInteger maxLimit) {
        BigInteger bigInteger = maxLimit.subtract(minLimit);
        SecureRandom randNum = new SecureRandom();
        int len = maxLimit.bitLength();
        BigInteger res = new BigInteger(len, randNum);
        boolean isPrime = false;
        while(isPrime!=true) {
            res = new BigInteger(len, randNum);
            if (res.compareTo(minLimit) < 0)
                res = res.add(minLimit);
            if (res.compareTo(bigInteger) >= 0)
                res = res.mod(bigInteger).add(minLimit);
            isPrime = FermaTest.checkPrime(res, 3);
        }
        return res;
    }
}
