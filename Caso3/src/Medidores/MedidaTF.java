package Medidores;

public class MedidaTF {
	
	private int booleano;
	private int id;
	
	//Si booleano es 1 es que fallo, si es 0 no.
	public MedidaTF(int nId,int nBooleano) {
		id=nId;
		booleano=nBooleano;
	}

	public int getBooleano() {
		return booleano;
	}

	public void setBooleano(int booleano) {
		this.booleano = booleano;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdS() {
		return id+"";
	}
	
	public String getBooleanoS() {
		return booleano+"";
	}
}
