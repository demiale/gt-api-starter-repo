package common.config;

import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;

public class CryptoUtils {

    public static String encrypt(String toEncrypt) {
        return getEncoder().encodeToString(toEncrypt.getBytes());
    }

    public static String decrypt(String toDecrypt) {
        return new String(getDecoder().decode(toDecrypt));
    }

}
