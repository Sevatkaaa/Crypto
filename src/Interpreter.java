import java.math.BigInteger;

public class Interpreter {

    private static final int SHIFT = 100;

    public static BigInteger interpretToNumber(String message) {
        final StringBuilder result = new StringBuilder();
        message.chars().forEach(symbol -> result.append(symbol + SHIFT));
        return new BigInteger(result.toString());
    }

    public static String interpretToWord(BigInteger m) {
        StringBuilder result = new StringBuilder();
        String numberInString = m.toString();
        while (!numberInString.isEmpty()) {
            String asciiCodeStr = numberInString.substring(0, 3);
            int asciiCode = Integer.parseInt(asciiCodeStr);
            result.append((char) (asciiCode - SHIFT));
            numberInString = numberInString.substring(3);
        }
        return result.toString();
    }
}
