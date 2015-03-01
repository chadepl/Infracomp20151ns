package nicolas;

public class Servidor extends Thread{
	
	private static int id;
	
	private static Buffer instancia;
	
	public Servidor(int nId, Buffer nInstancia){
		id=nId;
		instancia=nInstancia;
	}

	@Override
	public void run() {
		while(instancia.getNumeroClientes()>0){
			//lo que hace el buffer
			instancia.remover();
		}
		
	}

	
}
