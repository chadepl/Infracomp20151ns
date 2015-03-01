package sebastian;

public class Cliente extends Thread{
	
	private String id;
	private static Buffer buffer;
	private int numeroDeMensajesAEnviar;
	
	public Cliente(String id, Buffer buffer, int numeroDeMensajesAEnviar) {
		
		this.id = id;
		this.buffer = buffer;
		this.numeroDeMensajesAEnviar = numeroDeMensajesAEnviar;
		
	}
	
	public void run(){
		
		for(int i=1; i<=numeroDeMensajesAEnviar; i++) {
			
			Mensaje nuevoMensaje = new Mensaje(this.id+"-"+i, i);
			System.out.println("Se creo el nuevo mensaje ID: "+nuevoMensaje.getId()+" CONT: "+nuevoMensaje.getContenido());
			buffer.depositar(nuevoMensaje);
			System.out.println("Se editó el mensaje ID: "+nuevoMensaje.getId()+" CONT: "+nuevoMensaje.getContenido());
			
		}
		
		buffer.disminuirNumeroDeClientesRestantes();
		
	}

}
