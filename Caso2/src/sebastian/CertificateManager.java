package sebastian;

import java.math.BigInteger;
import java.sql.Date;

import javax.security.auth.x500.X500Principal;

import java.security.*;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V1CertificateGenerator;

public class CertificateManager {

	//La fecha de ayer
	private static Date validityBeginDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);

	// La fecha de venciemiento en un año
	private static Date validityEndDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000);

	public static X509Certificate generateX509Certificate(KeyPair keyPair) throws Exception {

		// GENERATE THE X509 CERTIFICATE
		X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
		X500Principal dnName = new X500Principal("CN=Cliente");

		certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		certGen.setSubjectDN(dnName);
		certGen.setIssuerDN(dnName); // use the same
		certGen.setNotBefore(validityBeginDate);
		certGen.setNotAfter(validityEndDate);
		certGen.setPublicKey(keyPair.getPublic());
		certGen.setSignatureAlgorithm("MD5WITHRSAENCRYPTION");

		return certGen.generate(keyPair.getPrivate(), "BC");

	}

	public static void verifyX509Certificate(X509Certificate certificate) throws Exception {

		PublicKey publicKey = certificate.getPublicKey();
		certificate.verify(publicKey);

	}

}
