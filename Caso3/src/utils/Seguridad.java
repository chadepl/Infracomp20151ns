/**
 * 
 */
package utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import servidor.Protocolo;

/**
 * Clase que contiene metodos static que proveen la seguridad del protocolo.
 * Infraestructura Computacional 201320
 * Universidad de los Andes.
 * Las tildes han sido eliminadas por cuestiones de compatibilidad.
 * @author Michael Andres Carrillo Pinzon
 */
public class Seguridad {

	/**
	 * Metodo que hace un cifrado simetrico delos bytes de entrada.
	 * @param msg El mensaje a encriptar.
	 * @param key La llave usada para encriptar.
	 * @param algo El algoritmo a encriptar.
	 * @return Los bytes cifrados que devolvio el algoritmo.
	 * @throws IllegalBlockSizeException Si hubo un error con el tama��o de la llave.
	 * @throws BadPaddingException Si hubo un error con el algoritmo.
	 * @throws InvalidKeyException Si la llave no es valida.
	 * @throws NoSuchAlgorithmException Si el algoritmo no es valido.
	 * @throws NoSuchPaddingException Si el padding no es valido.
	 */
	public static byte[] symmetricEncryption (byte[] msg, Key key , String algo)
			throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, 
			NoSuchAlgorithmException, NoSuchPaddingException {
		algo = algo + 
				(algo.equals(Protocolo.DES) || algo.equals(Protocolo.AES)?"/ECB/PKCS5Padding":"");
		Cipher decifrador = Cipher.getInstance(algo); 
		decifrador.init(Cipher.ENCRYPT_MODE, key); 
		return decifrador.doFinal(msg);
	}
	
	/**
	 * Metodo que hace un descifrado simetrico de los bytes de entrada.
	 * @param msg El mensaje a desencriptar.
	 * @param key La llave usada para encriptar.
	 * @param algo El algoritmo a encriptar.
	 * @return Los bytes cifrados que devolvio el algoritmo.
	 * @throws IllegalBlockSizeException Si hubo un error con el tama��o de la llave.
	 * @throws BadPaddingException Si hubo un error con el algoritmo.
	 * @throws InvalidKeyException Si la llave no es valida.
	 * @throws NoSuchAlgorithmException Si el algoritmo no es valido.
	 * @throws NoSuchPaddingException Si el padding no es valido.
	 */
	public static byte[] symmetricDecryption (byte[] msg, Key key , String algo)
			throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, 
			NoSuchAlgorithmException, NoSuchPaddingException {
		algo = algo + 
				(algo.equals(Protocolo.DES) || algo.equals(Protocolo.AES)?"/ECB/PKCS5Padding":"");
		Cipher decifrador = Cipher.getInstance(algo); 
		decifrador.init(Cipher.DECRYPT_MODE, key); 
		return decifrador.doFinal(msg);
	}
	
	/**
	 * Metodo que hace un cifrado asimetrico de los bytes de entrada.
	 * @param msg El mensaje a encriptar.
	 * @param key La llave usada para encriptar.
	 * @param algo El algoritmo a encriptar.
	 * @return Los bytes cifrados que devolvio el algoritmo.
	 * @throws IllegalBlockSizeException Si hubo un error con el tama��o de la llave.
	 * @throws BadPaddingException Si hubo un error con el algoritmo.
	 * @throws InvalidKeyException Si la llave no es valida.
	 * @throws NoSuchAlgorithmException Si el algoritmo no es valido.
	 * @throws NoSuchPaddingException Si el padding no es valido.
	 */
	public static byte[] asymmetricEncryption (byte[] msg, Key key , String algo) 
			throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, 
			NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher decifrador = Cipher.getInstance(algo); 
		decifrador.init(Cipher.ENCRYPT_MODE, key); 
		return decifrador.doFinal(msg);
	}
	
	/**
	 * Metodo que hace un descifrado simetrico de los bytes de entrada.
	 * @param msg El mensaje a desencriptar.
	 * @param key La llave usada para encriptar.
	 * @param algo El algoritmo a encriptar.
	 * @return Los bytes cifrados que devolvio el algoritmo.
	 * @throws IllegalBlockSizeException Si hubo un error con el tama��o de la llave.
	 * @throws BadPaddingException Si hubo un error con el algoritmo.
	 * @throws InvalidKeyException Si la llave no es valida.
	 * @throws NoSuchAlgorithmException Si el algoritmo no es valido.
	 * @throws NoSuchPaddingException Si el padding no es valido.
	 */
	public static byte[] asymmetricDecryption (byte[] msg, Key key , String algo) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
			IllegalBlockSizeException, BadPaddingException {
		Cipher decifrador = Cipher.getInstance(algo); 
		decifrador.init(Cipher.DECRYPT_MODE, key); 
		return decifrador.doFinal(msg);
	}
	
	/**
	 * Metodo que genera un codigo HMAC a partir de una llave, un mensaje y un algoritmo.
	 * @param msg El mensaje sobre el cual se va a aplicar el digest.
	 * @param key La llave que se usa para encriptar.
	 * @param algo El algoritmo que se va a utilizar para la encripcion.
	 * @return El digests en un arreglo de bytes.
	 * @throws NoSuchAlgorithmException Si el algoritmo no es valido.
	 * @throws InvalidKeyException Si la llave no es valida.
	 * @throws IllegalStateException Si no fue posible hacer el digest.
	 * @throws UnsupportedEncodingException Si la codificacion no es valida.
	 */
	public static byte[] hmacDigest(byte[] msg, Key key, String algo) throws NoSuchAlgorithmException,
			InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
		Mac mac = Mac.getInstance(algo);
		mac.init(key);

		byte[] bytes = mac.doFinal(msg);
		return bytes;
	}
	
	/**
	 * Metodo que verifica que un codigo HMAC corresponda con un mensaje dado.
	 * @param msg El mensaje que se quiere comprobar.
	 * @param key La llave simetrica con la cual se encripto.
	 * @param algo El algoritmo de generacion de HMAC.
	 * @param hash El hash que acompa��a al mensaje.
	 * @return La verificacion de que el mensaje y el codigo hmac coincidan.
	 * @throws Exception Si hubo un error al generar un mensaje HMAC.
	 */
	public static boolean verificarIntegridad(byte[] msg, Key key, String algo, byte [] hash ) throws Exception
	{
		byte [] nuevo = hmacDigest(msg, key, algo);
		if (nuevo.length != hash.length) {
			return false;
		}
		for (int i = 0; i < nuevo.length ; i++) {
			if (nuevo[i] != hash[i]) return false;
		}
		return true;
	}

	/**
	 * Metodo que se encarga de generar la llave simetrica de cualquier algoritmo.
	 * @param algoritmo - El algoritmo con el que va a encriptar la llave
	 * @return La llave simetrica.
	 * @throws NoSuchProviderException Si no hay un proveedor de seguridad.
	 * @throws NoSuchAlgorithmException Si el algoritmo no es valido.
	 */
	public static SecretKey keyGenGenerator(String algoritmo) 
			throws NoSuchAlgorithmException, NoSuchProviderException	{
		int tamLlave = 0;
		if (algoritmo.equals(Protocolo.DES))
			tamLlave = 64;
		else if (algoritmo.equals(Protocolo.AES))
			tamLlave = 128;
		else if (algoritmo.equals(Protocolo.BLOWFISH))
			tamLlave = 128;
		else if (algoritmo.equals(Protocolo.RC4))
			tamLlave = 128;
		
		if (tamLlave == 0) throw new NoSuchAlgorithmException();
		
		KeyGenerator keyGen;
		SecretKey key;
		keyGen = KeyGenerator.getInstance(algoritmo,"BC");
		keyGen.init(tamLlave);
		key = keyGen.generateKey();
		return key;
	}

	/**
	 * Metodo que crea un nuevo certificado digital siguiendo el formato X509 a partir del par de llaves dado usando las librerias de bouncycastle
	 * @param pair La pareja de llaves publica y privada necesarias para la generacion del certificado
	 * @return Un nuevo certificado autofirmado con formato X509.
	 * @throws InvalidKeyException Si las llaves no son validas.
	 * @throws NoSuchProviderException Si el proveedor de seguridad no esta bien establecido.
	 * @throws SignatureException Si no se pudo generar el certificado.
	 * @throws IllegalStateException Si no se pudo generar el certificado.
	 * @throws NoSuchAlgorithmException Si el algoritmo no es valido.
	 * @throws CertificateException Si no se pudo generar el certificado.
	 */
	public static java.security.cert.X509Certificate generateV3Certificate(KeyPair pair) throws InvalidKeyException,
	NoSuchProviderException, SignatureException, IllegalStateException, NoSuchAlgorithmException, CertificateException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		certGen.setIssuerDN(new X500Principal("CN=Test Certificate"));
		certGen.setNotBefore(new Date(System.currentTimeMillis() - 10000000));
		certGen.setNotAfter(new Date(System.currentTimeMillis() + 10000000));
		certGen.setSubjectDN(new X500Principal("CN=Test Certificate"));
		certGen.setPublicKey(pair.getPublic());
		certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

		certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(false));
		certGen.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature| KeyUsage.keyEncipherment));
		certGen.addExtension(X509Extensions.ExtendedKeyUsage, true, new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth));

		certGen.addExtension(X509Extensions.SubjectAlternativeName, false, new GeneralNames(
				new GeneralName(GeneralName.rfc822Name, "test@test.test")));
		return certGen.generate(pair.getPrivate(), "BC") ;
	}

	/**
	 * Metodo que genera el par de llaves de 1024 bits necesarias para la creacion del certificado
	 * @return El objeto que contiene tanto la llave publica como la privada
	 * @throws NoSuchAlgorithmException Si el algoritmo no es valido.
	 */
	public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {

		KeyPairGenerator kpGen = KeyPairGenerator.getInstance(Protocolo.RSA);
		kpGen.initialize(1024, new SecureRandom());
		return kpGen.generateKeyPair();
	}
}
