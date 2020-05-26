import java.math.BigInteger;

public class WordToNumberInterpretator {
    public static BigInteger interpretateToNumber(String message) {
        String result = "";
        for (int i = 0; i < message.length(); i++) {
            int c = message.charAt(i);
            result += Integer.toString(c);
        }
        return new BigInteger(result);
    }

    public static String  interpretateToWord(BigInteger m) {
        String result = "";
        String numberInString = m.toString();
        int numberLength = numberInString.length();
        for (int i = 0; i < numberLength;) {
            if (numberInString.charAt(i) == 1) {
                String asciiCodeStr = "";
                int asciiCode;
                asciiCodeStr += numberInString.charAt(i);
                asciiCodeStr += numberInString.charAt(i+1);
                asciiCodeStr += numberInString.charAt(i+2);
                asciiCode = Integer.parseInt(asciiCodeStr);
                result += Character.toString((char) asciiCode);
                i = i + 3;
            }
            else {
                String asciiCodeStr = "";
                int asciiCode;
                asciiCodeStr += numberInString.charAt(i);
                asciiCodeStr += numberInString.charAt(i + 1);
                asciiCode = Integer.parseInt(asciiCodeStr);
                result += Character.toString((char) asciiCode);
                i = i + 2;
            }
        }
        return result;
    }
}
