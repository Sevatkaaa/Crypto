package chat;

import rsa.Converter;
import rsa.RSAProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Group {
    private List<User> users = new ArrayList<>();

    public boolean addUser(User user) {
        BigInteger currentUserN = user.getN();
        AtomicBoolean found = new AtomicBoolean(true);
        users.forEach(anyUser -> {
            if (isNotCoprime(currentUserN, anyUser.getN())) {
                found.set(false);
            }
        });
        if (found.get()) {
            users.add(user);
        }
        return found.get();
    }

    private boolean isNotCoprime(BigInteger a, BigInteger b) {
        return !a.gcd(b).equals(BigInteger.ONE);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
    }

    public BigInteger createGroupMessage(String message) {
        BigInteger messageNumber = Converter.convertToNumber(message);
        List<BigInteger> ns = users.stream()
                .map(User::getN)
                .collect(Collectors.toList());
        List<BigInteger> keys = users.stream()
                .map(User::getEncryptionKey)
                .collect(Collectors.toList());
        List<BigInteger> encryptedMessageNumbers = new ArrayList<>();
        for (int i = 0; i < ns.size(); i++) {
            encryptedMessageNumbers.add(RSAProvider.decrypt(messageNumber, keys.get(i), ns.get(i)));
        }
        return getCommonMessage(encryptedMessageNumbers, ns);
    }

    private BigInteger getCommonMessage(List<BigInteger> c, List<BigInteger> modules) {
        if (c.size() == 1) {
            return c.get(0).mod(modules.get(0));
        }
        BigInteger c0 = c.remove(0);
        BigInteger c1 = c.remove(0);
        BigInteger mod0 = modules.remove(0);
        BigInteger mod1 = modules.remove(0);
        BigInteger mult1 = c0.multiply(mod1);
        BigInteger mult2 = c1.multiply(mod0);
        BigInteger res = mult2.subtract(mult1);
        BigInteger newM0 = mod0.multiply(mod1);
        BigInteger diff = mod0.subtract(mod1);
        BigInteger inverse = diff.modInverse(newM0);
        BigInteger x = inverse.multiply(res);
        c.add(0, x);
        modules.add(0, newM0);
        return getCommonMessage(c, modules);
    }
}
