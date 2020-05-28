package rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Generator {
    public static BigInteger generate(BigInteger minLimit, BigInteger maxLimit) {
        BigInteger difference = maxLimit.subtract(minLimit);
        Random randNum = new SecureRandom();
        int len = maxLimit.bitLength();
        BigInteger res = new BigInteger(len, randNum);
        boolean isPrime = false;
        while(!isPrime) {
            res = new BigInteger(len, randNum);
            res = res.mod(difference).add(minLimit);
            isPrime = FermaTest.checkPrime(res, 5);
        }
        return res;
    }
}
