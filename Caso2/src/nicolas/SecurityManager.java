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

}
