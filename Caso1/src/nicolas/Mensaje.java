package nicolas;

public class Mensaje {
	
	private String id;
	
	private int contenido;
	
	private boolean modificado;
	
	
	public Mensaje(String nId,int nContenido){
		id=nId;
		contenido=nContenido;
		modificado=false;
	}

	public int getContenido() {
		return contenido;
	}

	public void setContenido(int contenido) {
		this.contenido = contenido;
	}

	public synchronized void incrementar(){
		contenido++;
		modificado=true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isModificado() {
		return modificado;
	}

	public void setModificado(boolean modificado) {
		this.modificado = modificado;
	}
	
	

}
