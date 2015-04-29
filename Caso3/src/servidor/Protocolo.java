package servidor;

import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.security.cert.CertificateNotYetValidException;

import utils.Seguridad;
import utils.Transformacion;

/**
 * Esta clase implementa el protocolo que se realiza al recibir una conexión de un cliente.
 * Infraestructura Computacional Universidad de los Andes. 
 * Las tildes han sido eliminadas por cuestiones de compatibilidad.
 * 
 * @author Michael Andres Carrillo Pinzon 	-  201320.
 * @author José Miguel Suárez Lopera 		-  201510
 */
public class Protocolo {

	// ----------------------------------------------------
	// CONSTANTES DE CONTROL DE IMPRESION EN CONSOLA
	// ----------------------------------------------------
	public static final boolean SHOW_ERROR = true;
	public static final boolean SHOW_S_TRACE = false;
	public static final boolean SHOW_IN = false;
	public static final boolean SHOW_OUT = false;
	// ----------------------------------------------------
	// CONSTANTES PARA LA DEFINICION DEL PROTOCOLO
	// ----------------------------------------------------
	public static final String STATUS = "ESTADO";
	public static final String ACK = "INICIO";
	public static final String OK = "OK";
	public static final String ALGORITMOS = "ALGORITMOS";
	public static final String DES = "DES";
	public static final String AES = "AES";
	public static final String BLOWFISH = "Blowfish";
	public static final String RSA = "RSA";
	public static final String RC4 = "RC4";
	public static final String HMACMD5 = "HMACMD5";
	public static final String HMACSHA1 = "HMACSHA1";
	public static final String HMACSHA256 = "HMACSHA256";
	public static final String CERTSRV = "CERTSRV";
	public static final String CERCLNT = "CERCLNT";
	public static final String SEPARADOR = ":";
	public static final String HOLA = "HOLA";
	public static final String INIT = "INIT";
	public static final String ACT1 = "ACT1";
	public static final String ACT2 = "ACT2";
	public static final String RTA = "RTA";
	public static final String INFO = "INFO";
	public static final String ERROR = "ERROR";
	public static final String ERROR_FORMATO = "Error en el formato. Cerrando conexion";

	/**
	 * Metodo que se encarga de imprimir en consola todos los errores que se 
	 * producen durante la ejecuación del protocolo. 
	 * Ayuda a controlar de forma rapida el cambio entre imprimir y no imprimir este tipo de mensaje
	 */
	private static void printError(Exception e) {
		if(SHOW_ERROR)		System.out.println(e.getMessage());
		if(SHOW_S_TRACE) 	e.printStackTrace();	
	}

	/**
	 * Metodo que se encarga de leer los datos que envia el cliente.
	 *  Ayuda a controlar de forma rapida el cambio entre imprimir y no imprimir este tipo de mensaje
	 */
	private static String read(BufferedReader reader) throws IOException {
		String linea = reader.readLine();
		if(SHOW_IN)			System.out.println("<<CLNT: " + linea);
		return linea;
	}

	/**
	 * Metodo que se encarga de escribir los datos que el servidor envia el cliente.
	 *  Ayuda a controlar de forma rapida el cambio entre imprimir y no imprimir este tipo de mensaje
	 */
	private static void write(PrintWriter writer, String msg) {
		writer.println(msg);
		if(SHOW_OUT)		System.out.println(">>SERV: " + msg);
	}

	public static void atenderCliente(Socket s){
		try{
			PrintWriter writer = new PrintWriter(s.getOutputStream(), true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			// ////////////////////////////////////////////////////////////////////////
			// Recibe HOLA.
			// En caso de error de formato, cierra la conexion.
			// ////////////////////////////////////////////////////////////////////////

			String linea = read(reader);
			if (!linea.equals(HOLA)) {
				write(writer, ERROR_FORMATO);

				throw new FontFormatException(linea);
			}

			// ////////////////////////////////////////////////////////////////////////
			// Envia el status del servidor
			// ////////////////////////////////////////////////////////////////////////
			write(writer, ACK);

			linea = read(reader);
			if (!(linea.contains(SEPARADOR) && linea.split(SEPARADOR)[0].equals(ALGORITMOS))) {
				write(writer, ERROR_FORMATO);
				throw new FontFormatException(linea);
			}

			// Verificar los algoritmos enviados
			String[] algoritmos = linea.split(SEPARADOR);
			// Comprueba y genera la llave simetrica para comunicarse con el
			// servidor.
			if (!algoritmos[1].equals(DES) && !algoritmos[1].equals(AES) && !algoritmos[1].equals(BLOWFISH)
					&& !algoritmos[1].equals(RC4)) {

				write(writer, "ERROR:Algoritmo no soportado o no reconocido: " + algoritmos[1] + ". Cerrando conexion");
				throw new NoSuchAlgorithmException();
			}

			// Comprueba que el algoritmo asimetrico sea RSA.
			if (!algoritmos[2].equals(RSA)) {
				write(writer, "ERROR:Algoritmo no soportado o no reconocido: " + algoritmos[2] + ". Cerrando conexion");
				throw new NoSuchAlgorithmException();
			}
			// Comprueba que el algoritmo HMAC sea valido.
			if (!(algoritmos[3].equals(HMACMD5) || algoritmos[3].equals(HMACSHA1) || algoritmos[3]
					.equals(HMACSHA256))) {
				write(writer, "Algoritmo no soportado o no reconocido: " + algoritmos[3] + ". Cerrando conexion");
				throw new NoSuchAlgorithmException();
			}

			// Confirmando al cliente que los algoritmos son soportados.
			write(writer, STATUS + SEPARADOR + OK);

			// ////////////////////////////////////////////////////////////////////////
			// Recibiendo el certificado del cliente
			// ////////////////////////////////////////////////////////////////////////
			// byte[] receiver = new byte[7];
			// int read = s.getInputStream().read(receiver, 0, 7);

			linea = read(reader);
			if (!linea.equals(CERCLNT)) {
				write(writer, ERROR_FORMATO + ":" + linea);
				throw new FontFormatException(CERCLNT);
			}

			byte[] certificadoServidorBytes = new byte[520];
			s.getInputStream().read(certificadoServidorBytes);
			CertificateFactory creador = CertificateFactory.getInstance("X.509");
			InputStream in = new ByteArrayInputStream(certificadoServidorBytes);
			X509Certificate certificadoCliente = (X509Certificate) creador.generateCertificate(in);
			

			// ////////////////////////////////////////////////////////////////////////
			// Enviando el certificado del servidor.
			// ////////////////////////////////////////////////////////////////////////
			write(writer, CERTSRV);
			KeyPair keyPair = Seguridad.generateRSAKeyPair();
			java.security.cert.X509Certificate certSer = Seguridad.generateV3Certificate(keyPair);
			s.getOutputStream().write(certSer.getEncoded());
			s.getOutputStream().flush();
			

			// ////////////////////////////////////////////////////////////////////////
			// Genera llave simetrica y la envia al cliente
			// ////////////////////////////////////////////////////////////////////////
			SecretKey simetrica = Seguridad.keyGenGenerator(algoritmos[1]);
			byte[] ciphertext1 = Seguridad.asymmetricEncryption(simetrica.getEncoded(),
					certificadoCliente.getPublicKey(), algoritmos[2]);

			// Transforma la llave simertrica y la envia
			write(writer, INIT + SEPARADOR + Transformacion.codificar(ciphertext1));


			// ////////////////////////////////////////////////////////////////////////
			// Recibe la posicion del usuario.
			// ////////////////////////////////////////////////////////////////////////

			linea = read(reader);
			if (!(linea.contains(SEPARADOR) && linea.split(SEPARADOR)[0].equals(ACT1))) {
				write(writer, ERROR_FORMATO);
				throw new FontFormatException(linea);
			}
			String datos = new String(Seguridad.symmetricDecryption(
					Transformacion.decodificar(linea.split(SEPARADOR)[1]), simetrica, algoritmos[1]));

			linea = read(reader);
			if (!(linea.contains(SEPARADOR) && linea.split(SEPARADOR)[0].equals(ACT2))) {
				write(writer, ERROR_FORMATO);
				throw new FontFormatException(linea);
			}
			byte[] hmac = Seguridad.asymmetricDecryption(Transformacion.decodificar(linea.split(SEPARADOR)[1]),
					keyPair.getPrivate(), algoritmos[2]);
			// Evalua que el HMAC corresponda.
			boolean verificacion = Seguridad.verificarIntegridad(datos.getBytes(), simetrica, algoritmos[3], hmac);

			// ////////////////////////////////////////////////////////////////////////
			// Recibe el resultado de la transaccion y termina la conexion.
			// ////////////////////////////////////////////////////////////////////////
			write(writer, RTA + SEPARADOR + (verificacion? OK:ERROR));

			System.out.println("Termino requerimientos del cliente en perfectas condiciones.");
		} catch (NullPointerException e) {
			// Probablemente la conexion fue interrumpida.
			printError(e);
		} catch (IOException e) {
			// Error en la conexion con el cliente.
			printError(e);
		} catch (FontFormatException e) {
			// Si hubo errores en el protocolo por parte del cliente.
			printError(e);
		} catch (NoSuchAlgorithmException e) {
			// Si los algoritmos enviados no son soportados por el servidor.
			printError(e);
		} catch (NoSuchProviderException e) {
			// Error en adicionar el proveedor de seguridad.
			// No deberia alcanzarce en condiciones normales de ejecuci��n.
			printError(e);
		} catch (CertificateEncodingException e) {
			// El certificado no se pudo serializar.
			// No deberia alcanzarce en condiciones normales de ejecuci��n.
			printError(e);
		} catch (InvalidKeyException e) {
			// El certificado no se pudo generar.
			// No deberia alcanzarce en condiciones normales de ejecuci��n.
			printError(e);
		} catch (SignatureException e) {
			// El certificado no se pudo generar.
			// No deberia alcanzarce en condiciones normales de ejecuci��n.
			printError(e);
		} catch (IllegalStateException e) {
			// El certificado no se pudo generar.
			// No deberia alcanzarce en condiciones normales de ejecuci��n.
			printError(e);
		} catch (CertificateException e) {
			// El certificado no se pudo generar.
			// No deberia alcanzarce en condiciones normales de ejecuci��n.
			printError(e);
		} catch (CertificateNotYetValidException e) {
			// El certificado del cliente no se pudo recuperar.
			// El cliente deberia revisar la creacion y envio de su
			// certificado.
			printError(e);
		} catch (NoSuchPaddingException e) {
			// Error en el proceso de encripcion de datos del servidor.
			printError(e);
		} catch (IllegalBlockSizeException e) {
			// No se pudo generar un sobre digital sobre la llave simetrica.
			// No deberia alcanzarce en condiciones normales de ejecuci��n.
			printError(e);
		} catch (BadPaddingException e) {
			// No se pudo generar un sobre digital sobre la llave simetrica.
			// No deberia alcanzarce en condiciones normales de ejecuci��n.
			printError(e);
		} catch (Exception e) {
			// El cliente reporto que la informacion fue infructuosa.
			printError(e);
		} finally {
			try {
				s.close();
			} catch (Exception e) {
				// DO NOTHING
			}
		}
	}


}
