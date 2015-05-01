package servidorModificado;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.Security;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import Medidores.Medidor;



/**
 * Esta clase implementa el servidor que atiende a los clientes. El servidor 
 * esta implemntado como un pool de threads. Cada vez que un cliente crea
 * una conexion al servidor, un thread se encarga de atenderlo el tiempo que
 * dure la sesion. 
 * Infraestructura Computacional Universidad de los Andes. 
 * Las tildes han sido eliminadas por cuestiones de compatibilidad.
 * 
 * @author Michael Andres Carrillo Pinzon 	-  201320.
 * @author José Miguel Suárez Lopera 		-  201510
 */
public class Servidor extends Thread {

	/**
	 * Constante que especifica el tiempo máximo en milisegundos que se esperara 
	 * por la respuesta de un cliente en cada una de las partes de la comunicación
	 */
	private static final int TIME_OUT = 10000;
	
	private static final int MEDIDAS=2;

	/**
	 * Constante que especifica el numero de threads que se usan en el pool de conexiones.
	 */

	public static final int N_THREADS1=1;
	public static final int N_THREADS2=2;
	public static final int N_THREADS3=4;
	public static final int N_THREADS4=16;
	
	/**
	 * Puerto en el cual escucha el servidor.
	 */
	public static final int PUERTO = 8000;

	/**
	 * El socket que permite recibir requerimientos por parte de clientes.
	 */
	private static ServerSocket socket;


	/**
	 * El id del Thread
	 */
	private int id;

	/**
	 * Metodo main del servidor con seguridad que inicializa un 
	 * pool de threads determinado por la constante nThreads.
	 * @param args Los argumentos del metodo main (vacios para este ejemplo).
	 * @throws IOException Si el socket no pudo ser creado.
	 */
	public static void main(String[] args) throws IOException {

		
		ExecutorService executor=Executors.newFixedThreadPool(N_THREADS1);
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		socket = new ServerSocket(PUERTO);
		int i=0;
		while(MEDIDAS!=i){
			try {
				Socket s = socket.accept();
				s.setSoTimeout(TIME_OUT);
				System.out.println("Se conecto");
				Runnable run=new Runnable() {
					public void run() {
						Protocolo.atenderCliente(s);
					}
				};
				executor.execute(run);
				i++;
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
		
	
		
		
		

		//executor.shutdown();
		//while(!executor.isTerminated()){}
		
	}

	



}
