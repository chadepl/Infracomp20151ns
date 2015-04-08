package nicolas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecurityManager {
	
	private static final String ALGORITMO="RSA";
	private static final String ALGORITMO_CIFRADO="AES";
	
	
	private KeyPair keyPair;
	
	public SecurityManager(KeyPair keyPair) {
		// TODO Auto-generated constructor stub
		this.keyPair=keyPair;
	}
	
	public byte[] cifrar(String pwd, SecretKey llave) {
		try {
			
			Cipher cipher = Cipher.getInstance(ALGORITMO_CIFRADO);
			byte[] clearText = pwd.getBytes();
			
			String s1 = new String(clearText);
			System.out.println("clave original: " + s1);
			
			cipher.init(Cipher.ENCRYPT_MODE, llave);
			long startTime = System.nanoTime();
			byte[] cipheredText = cipher.doFinal(clearText);
			
			long endTime = System.nanoTime();
			System.out.println("clave cifrada: " + cipheredText);
			System.out.println("Tiempo asimetrico: " + (endTime - startTime));
			return cipheredText;
		} catch (Exception e) {
			System.out.println("Excepcion: " + e.getMessage());
			return null;
		}
	}
	
	public String descifrar(byte[] cipheredText) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITMO);
			cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
			byte[] clearText = cipher.doFinal(cipheredText);
			String s3 = new String(clearText);
			System.out.println("clave original: " + s3);
			return s3;
		} catch (Exception e) {
			System.out.println("Excepcion: " + e.getMessage());
			return null;
		}
	}
	

	
	 public static String getHexString(byte[] b) throws Exception {
	        String result = "";
	        for (int i=0; i < b.length; i++) {
	            result +=
	                Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	        }
	        return result;
	    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}
