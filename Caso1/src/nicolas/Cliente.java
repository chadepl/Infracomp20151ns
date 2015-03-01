package nicolas;


public class Cliente extends Thread{
	
	private static int id;
	
	private static int mensajesRestantes;
	
	private static Buffer instancia;
	
	private static int mensajesEnviados;
	
	public Cliente(int nId,int nMensajes, Buffer nInstancia){
		id=nId;
		mensajesRestantes=nMensajes;
		instancia=nInstancia;
		mensajesEnviados=0;
	}

	@Override
	public void run() {
		while(mensajesRestantes>0){
			
			mensajesRestantes--;
			mensajesEnviados++;
			Mensaje mensaje=new Mensaje(id+"-"+mensajesEnviados,((int)(Math.random()*10)));
			System.out.println("Mensaje inicial del thread con id: "+mensaje.getId()+" es: "+mensaje.getContenido());
			instancia.insertar(mensaje);//Lo que va a hacer el buffer 
			System.out.println("Mensaje final del thread con id: "+mensaje.getId()+" es: "+mensaje.getContenido());
			
			
		}
		
		instancia.disminuirClientes();
		
		
	}
	
	public static void modificarMensaje(){
//		int contenidoNuevo=mensaje.getContenido()+1;
//		mensaje.setContenido(contenidoNuevo);
	}
	

}
