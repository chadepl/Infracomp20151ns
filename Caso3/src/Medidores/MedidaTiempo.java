package Medidores;

public class MedidaTiempo {
	
	private int id;
	private Long tiempoInicial;
	private Long tiempoFinal;
	private Long tiempoTranscurrido;
	
	public MedidaTiempo(int nId,Long nTI,Long nTF){
		id=nId;
		tiempoInicial=nTI;
		tiempoFinal=nTF;
		tiempoTranscurrido=tiempoFinal-tiempoInicial;
	}

}
