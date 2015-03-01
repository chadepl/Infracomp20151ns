package nicolas;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Buffer {
	
	private int numeroClientes;
	
	private LinkedList<Mensaje> cola;
	
	private int capacidad;
	
	public Buffer(int nNumeroClientes, int nCapacidad){
		capacidad=nCapacidad;
		numeroClientes=nNumeroClientes;
		cola=new LinkedList<Mensaje>();
	}
	
	//Metodo para los productores
	public void insertar(Mensaje mensaje){
		while(cola.size()==capacidad){
			Thread.yield();
		}
		
		synchronized (this) {
			cola.add(mensaje);
			capacidad--;
			this.notifyAll();
		}
		
		try {
			synchronized (mensaje) {
				if(!mensaje.isModificado()) {
					mensaje.wait();
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//Metodo para los consumidores
	public synchronized void remover(){
		while(cola.isEmpty() && numeroClientes>0){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (!cola.isEmpty()) {
			Mensaje atender = cola.poll();
			atender.incrementar();
			capacidad++;

			//Para solucionar el problema de quien es el dueño del monitor
			synchronized (atender) {
				atender.notify();
			}
		}
		
		
		
		
		
	}
	
	
	public synchronized void disminuirClientes(){
		numeroClientes--;
		//Para que ningun servidor se vaya a quedar dormido
		if(numeroClientes==0){
			this.notifyAll();
		}
	}
	
	public int getNumeroClientes(){
		return numeroClientes;
	}
	
	

}
