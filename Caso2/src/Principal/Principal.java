package Principal;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;


public class Principal {

	private static Socket s;
	private static PrintStream p;
	private static InputStream l;
	private static BufferedReader r;
	
	
	public static void main(String[] args) {
		String serverName="infracomp.virtual.uniandes.edu.co";
		int port=80;
		try {
			s = new Socket(serverName,port);
			System.out.println("CONECTADO A: "+s.getRemoteSocketAddress());
			p = new PrintStream(s.getOutputStream());
			r=new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			p.println("HOLA");
			
			if(r.readLine().equals("INICIO")){
				p.println("ALGORITMOS:AES:RSA:HMACMD5");
				if(r.readLine().equals("ESTADO:OK")){
					p.println("CERTCLNT");
					java.security.cert.X509Certificate cert=certificado();
					byte[] mybyte=cert.getEncoded();
					s.getOutputStream().write(mybyte);
					s.getOutputStream().flush();
					
				}else{
					System.out.println("Error");
				}
			}else{
				System.out.println("Error");
			}
			s.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	private static X509Certificate certificado() {
		// TODO Auto-generated method stub
		
		return null;
	}
}
