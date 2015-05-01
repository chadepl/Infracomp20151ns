package generadorCarga;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Generator {

	///////////////////
	//ESCENARIO 1//////
	///////////////////
	private static final int NUM_TRANSACCIONES1=400;
	private static final int GAP_TRANSACCION1=20;
	
	///////////////////
	//ESCENARIO 2//////
	///////////////////
	private static final int NUM_TRANSACCIONES2=200;
	private static final int GAP_TRANSACCION2=40;
	
	///////////////////
	//ESCENARIO 3//////
	///////////////////
	private static final int NUM_TRANSACCIONES3=80;
	private static final int GAP_TRANSACCION3=100;
	
	private LoadGenerator generator;
	private static ArrayList<Medida> medidas;
	private static int numeroTransaccionesPerdidas;

	public Generator(int escenario){
		
		int numberOfTasks=0;
		int gapBetweenTasks=0;

		medidas = new ArrayList();
		numeroTransaccionesPerdidas = 0;
		
		if(escenario==1){
			numberOfTasks=NUM_TRANSACCIONES1;
			gapBetweenTasks=GAP_TRANSACCION1;
		}else if(escenario==2){
			numberOfTasks=NUM_TRANSACCIONES2;
			gapBetweenTasks=GAP_TRANSACCION2;
		}else if(escenario==3){
			numberOfTasks=NUM_TRANSACCIONES3;
			gapBetweenTasks=GAP_TRANSACCION3;
		}
		Task work=createTask();
		generator=new LoadGenerator("Client - Server load test", numberOfTasks, work, gapBetweenTasks);
		generator.generate();

	}

	public static synchronized void guardarInformacion(long nTiempoAutenticacion, long nTiempoFin, String idCliente) {

		Medida medida = new Medida(nTiempoAutenticacion,nTiempoFin,idCliente);
		medidas.add(medida);

	}

	public static synchronized void aumentarNumeroTransaccionesPerdidas() {

		numeroTransaccionesPerdidas++;
	}

	private Task createTask() {
		// TODO Auto-generated method stub
		return new ClientServerTask();
		//return null;
	}
	
	public static void main(String[] args) {
		Generator generator=new Generator(3);
	}
	
	
}
