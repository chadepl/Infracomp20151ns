package generadorCarga;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

public class Generator {

	
	private LoadGenerator generator;
	
	public Generator(){
		Task work=createTask();
		int numberOfTasks=100;
		int gapBetweenTasks=1000;
		generator=new LoadGenerator("Client - Server load test", numberOfTasks, work, gapBetweenTasks);
		generator.generate();
	}

	private Task createTask() {
		// TODO Auto-generated method stub
		//return ClientServerTask;
		return null;
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Generator generator=new Generator();
	}
	
	
}
