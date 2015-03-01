package sebastian;

public class Servidor extends Thread{
	
	private static Buffer buffer;
	
	public Servidor(Buffer buffer) {
		this.buffer = buffer;
	}
	
	public void run() {
		
		while(buffer.getNumeroDeClientesRestantes() > 0) {
			
			buffer.retirar();
			//System.out.println("sigue vivo");
			
		}
		
	}

}
