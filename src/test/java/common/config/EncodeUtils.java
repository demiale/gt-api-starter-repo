package common.config;

import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;

class EncodeUtils {

    static String encode(String toEncrypt) {
        return getEncoder().encodeToString(toEncrypt.getBytes());
    }

    static String decode(String toDecrypt) {
        return new String(getDecoder().decode(toDecrypt));
    }

}
