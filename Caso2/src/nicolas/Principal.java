package nicolas;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.security.KeyPair;
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

	private static String serverName = "localhost";
	private static int port;
	private static Socket s;
	private static PrintStream p;
	private static InputStream l;
	private static BufferedReader r;
	
	private static CertificateManager cm;

	private static boolean iniciarSesion() throws Exception {

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
		X509Certificate clientCertificate = cm.generateX509Certificate();
		byte[] clientCertificateBytes = clientCertificate.getEncoded();
		s.getOutputStream().write(clientCertificateBytes);
		s.getOutputStream().flush();

		byte[] cervtSrvBytes = new byte[8];
		s.getInputStream().read(cervtSrvBytes, 0, 8);
		String certSrvString = new String(cervtSrvBytes);
		
		
		
		if(certSrvString.equals(CERTSRV+"\n")) {
			
			
			//Obtengo el certificado del servidor 
			
			byte[] serverCertificateBytes = new byte[16478];
			s.getInputStream().read(serverCertificateBytes, 0, 520);
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			
			InputStream in = new ByteArrayInputStream(serverCertificateBytes);
			X509Certificate serverCertificate = (X509Certificate)certFactory.generateCertificate(in);
			System.out.println(serverCertificate);
			
			//Leo la linea de la llave que manda el servidor
			byte[] inicio = new byte[5];
			s.getInputStream().read(inicio, 0, 5);
			String inicioString = new String(inicio);
			inicioString=inicioString.split(":")[0];
			System.out.println(inicioString);
			
			byte[] llave = new byte[256];
			s.getInputStream().read(llave,6,250);
			String llaveString = new String(llave);
			System.out.println(llaveString);
			
			if(inicioString.equals(INIT)){
				System.out.println("Me llego init");
				//System.out.println(initllave[1]);
				SecurityManager sm=new SecurityManager(cm.getKeyPair());
				System.out.println("Llave: "+sm.getHexString(llave));
				//sm.descifrar(cipheredText);
			}
			
			//
			byte[] mensaje = new byte[100];
			s.getInputStream().read(mensaje, 0, 100);
			CipherExtra cipher=new CipherExtra();
			//cipher.
			System.out.println(mensaje);
			in.close();
			
			String ln = r.readLine();
			
			return ln.equals(INIT);
		}
		
		return false;

	}



	public static boolean noSecurity() {

		port=443;

		try {
			s = new Socket(serverName, port);
			System.out.println("CONECTADO A: "+s.getRemoteSocketAddress());
			p = new PrintStream(s.getOutputStream());
			r=new BufferedReader(new InputStreamReader(s.getInputStream()));

			p.println("HOLA");

			if(iniciarSesion()) {

				if(authenticate()) {

					p.println(ACT1);
					p.println(ACT2);

					String[] ln = r.readLine().split(":");
					
					s.close();
					if(ln[0].equals(RTA) && ln[1].equals(OK)) {
						return true;
					}

				}
			}
			
			s.close();
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;

	}


	public static void main(String[] args) {
		cm=new CertificateManager();
		System.out.println(noSecurity());

	}

}
