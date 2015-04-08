package sebastian;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.util.encoders.Base64;


public class Cipher {
	
	private KeyPair key;
	private PrivateKey priv;
	private PublicKey pub;
	
	
	private void generateKeys() throws NoSuchAlgorithmException{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        key = keyGen.generateKeyPair();
        priv = key.getPrivate();
        pub = key.getPublic();
        //String privateKey = new String(Base64.encode(priv.getEncoded(), 0,priv.getEncoded().length, Base64.NO_WRAP));
        //String publicKey1 = new String(Base64.encode(pub.getEncoded(), 0,pub.getEncoded().length, Base64.NO_WRAP));
        //String publicKey = new String(Base64.encode(publicKey1.getBytes(),0, publicKey1.getBytes().length, Base64.NO_WRAP));
	}

	public void decrypt (String privateKeyFilename, String encryptedFilename, String outputFilename){

        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            String key = readFileAsString(privateKeyFilename);
            Base64 b64 = new Base64();
            AsymmetricKeyParameter privateKey = 
                (AsymmetricKeyParameter) PrivateKeyFactory.createKey(b64.decode(key));
            AsymmetricBlockCipher e = new RSAEngine();
            e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
            e.init(false, privateKey);

            String inputdata = readFileAsString(encryptedFilename);
            byte[] messageBytes = hexStringToByteArray(inputdata);
            byte[] hexEncodedCipher = e.processBlock(messageBytes, 0, messageBytes.length);

            System.out.println(new String(hexEncodedCipher));
            BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
            out.write(new String(hexEncodedCipher));
            out.close();

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
	
	public void decryptClassKey (String privateKeyFilename, String encryptedFilename, String outputFilename){

        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            String key = readFileAsString(privateKeyFilename);
            Base64 b64 = new Base64();
            AsymmetricKeyParameter privateKey = 
                (AsymmetricKeyParameter) PrivateKeyFactory.createKey(b64.decode(key));
            AsymmetricBlockCipher e = new RSAEngine();
            e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
            e.init(false, privateKey);

            String inputdata = readFileAsString(encryptedFilename);
            byte[] messageBytes = hexStringToByteArray(inputdata);
            byte[] hexEncodedCipher = e.processBlock(messageBytes, 0, messageBytes.length);

            System.out.println(new String(hexEncodedCipher));
            BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
            out.write(new String(hexEncodedCipher));
            out.close();

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
   
    public String decrypt (String privateKeyFilename, String encryptedData) {

        String outputData = null;
        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            String key = readFileAsString(privateKeyFilename);
            Base64 b64 = new Base64();
            AsymmetricKeyParameter privateKey = 
                (AsymmetricKeyParameter) PrivateKeyFactory.createKey(b64.decode(key));
            AsymmetricBlockCipher e = new RSAEngine();
            e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
            e.init(false, privateKey);

            byte[] messageBytes = hexStringToByteArray(encryptedData);
            byte[] hexEncodedCipher = e.processBlock(messageBytes, 0, messageBytes.length);

            System.out.println(new String(hexEncodedCipher));
            outputData = new String(hexEncodedCipher);

        }
        catch (Exception e) {
            System.out.println(e);
        }
       
        return outputData;
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

    private static String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        System.out.println(fileData.toString());
        return fileData.toString();
    }
    


}
