package sebastian;

import java.util.*;

import javax.sql.rowset.spi.SyncResolver;

public class Buffer {

	private Integer capacidad;
	private ArrayList<Mensaje> buffer;
	private Integer numeroDeClientesRestantes;

	public Buffer(int capacidad, int numeroDeClientesRestantes) {
		this.capacidad = capacidad;
		this.numeroDeClientesRestantes = numeroDeClientesRestantes;
		buffer = new ArrayList<Mensaje>();
	}


	public Integer getNumeroDeClientesRestantes() {
		return numeroDeClientesRestantes;
	}



	public void depositar(Mensaje mensaje) {

		while(buffer.size()==capacidad) {
			//System.out.println("Espera Activa");
			Thread.yield();
		}
		//System.out.println("Salio");
		synchronized (this) {
			buffer.add(mensaje);
			capacidad--;
			this.notifyAll();
		}
		//Muy pocas veces un servidor le alcanza a retirar el mensaje antes de quedarse dormido
		try {
			synchronized (mensaje) {
				if(!mensaje.isProcesado()) {
					mensaje.wait();
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	public void retirar() {

		synchronized (this) {
			while (buffer.size() == 0 && numeroDeClientesRestantes>0) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//System.out.println("Salio retirar");

		//synchronized (this) {
		Mensaje mensaje = new Mensaje("falso", 0);
		//if (buffer.size()>0) {

		synchronized (this) {
			if (buffer.size()>0) {
				mensaje = buffer.remove(0);
				mensaje.incrementarValor();
				capacidad++;
			}
		}

		synchronized (mensaje) {
			mensaje.notify();
		}

		//}
		//}

	}

	public synchronized void disminuirNumeroDeClientesRestantes() {

		numeroDeClientesRestantes--;
		System.out.println("Termino: "+numeroDeClientesRestantes);

		this.notifyAll();

	}


}
