package generadorCarga;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

public class Generator {

	///////////////////
	//ESCENARIO 1//////
	///////////////////
	private static final int NUM_TRANSACCIONES1=2;
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
	
	public Generator(int escenario){
		
		int numberOfTasks=0;
		int gapBetweenTasks=0;
		
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

	private Task createTask() {
		// TODO Auto-generated method stub
		return new ClientServerTask();
		//return null;
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Generator generator=new Generator(1);
	}
	
	
}
