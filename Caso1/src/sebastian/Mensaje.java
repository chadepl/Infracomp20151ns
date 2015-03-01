package sebastian;

public class Mensaje {
	
	private String id;
	private int contenido;
	private boolean procesado;
	
	public Mensaje(String id, int nContenido){
		this.id = id;
		contenido=nContenido;
		procesado=false;
	}

	public int getContenido() {
		return contenido;
	}

	public synchronized void incrementarValor() {
		this.contenido++;
		procesado=true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isProcesado() {
		return procesado;
	}
	
}
