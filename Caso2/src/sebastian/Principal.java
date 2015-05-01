package sebastian;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;



public class Principal {

	private static final String HOLA = "HOLA";
	private static final String INICIO = "INICIO";
	private static final String ALGORITMOS = "ALGORITMOS:AES:RSA:HMACMD5";
	private static final String ESTADO = "ESTADO";
	private static final String OK = "OK";
	private static final String CERCLNT = "CERCLNT";
	private static final String CERTSRV = "CERTSRV";
	private static final String INIT = "INIT";
	private static final String ACT1 = "ACT1";
	private static final String ACT2 = "ACT2";
	private static final String RTA = "RTA";

	private static String serverName = "localhost";//private static String serverName = "infracomp.virtual.uniandes.edu.co";
	private static int port;
	private static Socket s;
	private static PrintStream p;
	private static BufferedReader r;
	private static KeyPair keyPair;
	private static PublicKey serverPublicKey;
	private static SecretKey secretKey;

	private static boolean signIn() throws Exception {

		s = new Socket(serverName, port);
		p = new PrintStream(s.getOutputStream());
		r = new BufferedReader(new InputStreamReader(s.getInputStream()));

		p.println(HOLA);
		String ln = r.readLine();
		if(ln.equals(INICIO)) {

			p.println(ALGORITMOS);
			String lns[] = r.readLine().split(":");
			if(lns[0].equals(ESTADO) && lns[1].equals(OK)) {
				return true;
			}

		}

		return false;
	}

	private static boolean authenticate() throws Exception{

		p.println(CERCLNT);

		keyPair = SecurityManager.generateKeyPair();

		X509Certificate clientCertificate = CertificateManager.generateX509Certificate(keyPair);
		byte[] clientCertificateBytes = clientCertificate.getEncoded();
		s.getOutputStream().write(clientCertificateBytes);
		s.getOutputStream().flush();

		String ln = r.readLine();

		if(ln.equals(CERTSRV)) {

			byte[] serverCertificateBytes = new byte[520];
			s.getInputStream().read(serverCertificateBytes, 0, 520);
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

			InputStream in = new ByteArrayInputStream(serverCertificateBytes);
			X509Certificate serverCertificate = (X509Certificate)certFactory.generateCertificate(in);
			in.close();

			CertificateManager.verifyX509Certificate(serverCertificate);

			serverPublicKey = serverCertificate.getPublicKey();

			String lns[] = r.readLine().split(":");

			if(lns[0].equals(INIT)) {
				storePrivateKey(lns[1]);
				return true;
			}
		}

		return false;

	}

	private static void storePrivateKey(String ln) throws Exception {

		byte[] encodedKeyBytes = SecurityManager.hexStringToByteArray(ln);
		byte[] decryptedKeyBytes = SecurityManager.decrypt(encodedKeyBytes, keyPair.getPrivate(), "RSA");

		secretKey = new SecretKeySpec(decryptedKeyBytes, "AES");

	}

	public static boolean withSecurity() {

		port = 1024;

		try {
			s = new Socket(serverName, port);
			System.out.println("CONECTADO A: "+s.getRemoteSocketAddress());
			p = new PrintStream(s.getOutputStream());
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));

			p.println("HOLA");

			if(signIn()) {

				if(authenticate()) {

					return reportPosition();

				}
			}

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	private static boolean reportPosition() throws Exception{

		String longitude = "-74.0641626";
		String latitude = "4.6015086";
		String fullPosition = latitude +","+ longitude;
		byte[] fullPositionBytes = fullPosition.getBytes();
		byte[] encryptedFullPosition = SecurityManager.encrypt(fullPositionBytes, secretKey, "AES/ECB/PKCS5Padding");
		String encryptedFullPositionInHex = SecurityManager.getHexStringFromBytes(encryptedFullPosition);
		p.println(ACT1+":"+encryptedFullPositionInHex);

		byte[] hashBytes = SecurityManager.createHmac(fullPositionBytes, secretKey, "HMACMD5");
		byte[] encryptedHashBytes = SecurityManager.encrypt(hashBytes, serverPublicKey, "RSA");
		String encryptedHashInHex = SecurityManager.getHexStringFromBytes(encryptedHashBytes);
		p.println(ACT2+":"+encryptedHashInHex);

		String ln = r.readLine();

		return ln.equals(RTA+":"+OK);


	}


	public static boolean noSecurity() {

		port=1024;

		try {
			s = new Socket(serverName, port);
			System.out.println("CONECTADO A: "+s.getRemoteSocketAddress());
			p = new PrintStream(s.getOutputStream());
			r=new BufferedReader(new InputStreamReader(s.getInputStream()));

			p.println("HOLA");

			if(signIn()) {

				if(authenticate()) {

					p.println(ACT1);
					p.println(ACT2);

					String[] ln = r.readLine().split(":");

					if(ln[0].equals(RTA) && ln[1].equals(OK)) {
						return true;
					}

				}
			}

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}


	public static void main(String[] args) {

		System.out.println(withSecurity());
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public Principal() {
		
		System.out.println(withSecurity());
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
