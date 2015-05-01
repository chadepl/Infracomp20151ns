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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getTiempoInicial() {
		return tiempoInicial;
	}

	public void setTiempoInicial(Long tiempoInicial) {
		this.tiempoInicial = tiempoInicial;
	}

	public Long getTiempoFinal() {
		return tiempoFinal;
	}

	public void setTiempoFinal(Long tiempoFinal) {
		this.tiempoFinal = tiempoFinal;
	}

	public Long getTiempoTranscurrido() {
		return tiempoTranscurrido;
	}

	public void setTiempoTranscurrido(Long tiempoTranscurrido) {
		this.tiempoTranscurrido = tiempoTranscurrido;
	}

	public String getIdS() {
		// TODO Auto-generated method stub
		return id+"";
	}
	
	public String getTiempoInicialS() {
		return tiempoInicial+"";
	}
	
	public String getTiempoFinalS() {
		return tiempoFinal+"";
	}
	
	public String getTiempoTranscurridoS() {
		return tiempoTranscurrido+"";
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id+" : "+tiempoInicial+" : "+tiempoInicial+" : "+tiempoTranscurrido;
	}

}
