package nicolas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;

public class SecurityManager {
	
	private static final String ALGORITMO="RSA";
	
	
	private KeyPair keyPair;
	
	public SecurityManager(KeyPair keyPair) {
		// TODO Auto-generated constructor stub
		this.keyPair=keyPair;
	}
	
	public byte[] cifrar() {
		try {
			
			Cipher cipher = Cipher.getInstance(ALGORITMO);
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(
					System.in));
			String pwd = stdIn.readLine();
			byte[] clearText = pwd.getBytes();
			String s1 = new String(clearText);
			System.out.println("clave original: " + s1);
			cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
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
	
	public void descifrar(byte[] cipheredText) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITMO);
			cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
			byte[] clearText = cipher.doFinal(cipheredText);
			String s3 = new String(clearText);
			System.out.println("clave original: " + s3);
		} catch (Exception e) {
			System.out.println("Excepcion: " + e.getMessage());
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
