package nicolas;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Date;

import javax.security.auth.x500.X500Principal;

import java.security.*;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.bouncycastle.x509.X509V3CertificateGenerator;

public class CertificateManager {

	private static KeyPair keyPair;
	
	//La fecha de ayer
	private static Date validityBeginDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);

	// La fecha de venciemiento en un año
	private static Date validityEndDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000);
	
	public CertificateManager() {
		// TODO Auto-generated constructor stub
	}

	public static X509Certificate generateX509Certificate() throws Exception {

		// GENERATE THE PUBLIC/PRIVATE RSA KEY PAIR
		Security.addProvider(new BouncyCastleProvider());
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
		keyPairGenerator.initialize(1024, new SecureRandom());

		keyPair = keyPairGenerator.generateKeyPair();
		
		// GENERATE THE X509 CERTIFICATE
	    X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
	    X500Principal dnName = new X500Principal("CN=Cliente");

	    certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
	    certGen.setSubjectDN(dnName);
	    certGen.setIssuerDN(dnName); // use the same
	    certGen.setNotBefore(validityBeginDate);
	    certGen.setNotAfter(validityEndDate);
	    certGen.setPublicKey(keyPair.getPublic());
	    certGen.setSignatureAlgorithm("MD5WITHRSAENCRYPTION");

	    X509Certificate cert = certGen.generate(keyPair.getPrivate(), "BC");
	    
	    
	    return cert;

	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	
	
	
}
