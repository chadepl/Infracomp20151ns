package sebastian;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.security.*;

public class SecurityManager {

    public static byte[] hexStringToByteArray(String hexSting) {

        int len = hexSting.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexSting.charAt(i), 16) << 4) + Character.digit(hexSting.charAt(i + 1), 16));
        }

        return data;

    }

    public static byte[] decrypt(byte[] encodedData, PrivateKey privateKey, String algorithm) throws Exception {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(encodedData);
    }

    public static byte[] encrypt(byte[] data, Key key, String padding) throws Exception {

        Cipher cipher = Cipher.getInstance(padding);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(data);
    }

    public static KeyPair generateKeyPair() throws Exception{

        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(1024, new SecureRandom());

        return keyPairGenerator.generateKeyPair();

    }

    public static String getHexStringFromBytes(byte[] bytes) throws Exception {

        String result = "";
        for (int i=0; i < bytes.length; i++) {
            result += Integer.toString( ( bytes[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;

    }

    public static byte[] createHmac(byte[] data, SecretKey secretKey, String algorithm) throws Exception {

        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKey);

        return mac.doFinal(data);

    }

}
