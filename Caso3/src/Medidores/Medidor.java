package Medidores;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.csvreader.CsvWriter;

public class Medidor {
	
	
	private String titulo;
	
	//Arreglo de medidas para los tiempos 
	private MedidaTiempo[] tiempos;
	//Arreglo de medidas fallidas
	private MedidaTF[] fallidas;
	
	private String tipoMedidaTomando;
	
	private int carga;
	
	private int threads;
	
	//El titulo se refiere al titulo de la tabla 
	public Medidor(String tipo,String nTitulo, int nCarga, int nThreads){
		
		tipoMedidaTomando=tipo;
		if(tipo.equals("TIEMPOS")){
			tiempos=new MedidaTiempo[nCarga];
			System.out.println("Largo de TIEMPOS: "+tiempos.length);
		}else if(tipo.equals("TF")){
			fallidas= new MedidaTF[nCarga];
		}else{
			System.out.println("ERROR");
		}
		
		titulo=nTitulo;
		carga=nCarga;
		threads=nThreads;
		
	}
	

	public synchronized MedidaTiempo tomarMedidaTiempo(int id, Long tiempoInicial, Long tiempoFinal){
		tiempos[id]=new MedidaTiempo(id, tiempoInicial, tiempoFinal);
		return tiempos[id];
	}
	
	public void tomarMedidaTF(int id,int booleano){
		fallidas[id]=new MedidaTF(id,booleano);
	}
	
	//El escenario puede ir desde 1 hasta 12, son los escenarios propuestos en el enunciado
	public void exportarCSV(int escenario){
		
		String outputFile="";
		String rutaBase="dist/";
		
		if(tipoMedidaTomando.equals("TIEMPOS")){
			if(escenario>=1 && escenario<=12){
				outputFile=rutaBase+"tiempos"+escenario+".csv";
			}else{
				System.out.println("ERROR");
			}
		}else if(tipoMedidaTomando.equals("TF")){
			if(escenario>=1 && escenario<=12){
				outputFile=rutaBase+"transaccionesfallidas"+escenario+".csv";
			}else{
				System.out.println("ERROR");
			}
		}
		
		// before we open the file check to see if it already exists
		boolean alreadyExists = new File(outputFile).exists();
			
		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
			
			// if the file didn't already exist then we need to write out the header line
//			if (!alreadyExists)
//			{
//				csvOutput.write("id");
//				csvOutput.write("name");
//				csvOutput.endRecord();
//			}
			// else assume that the file already has the correct header line
			
			if(tipoMedidaTomando.equals("TIEMPOS")){
				//Falta hacer el header del documento 
				for(int i=0;i<carga;i++){
					csvOutput.write(tiempos[i].getIdS());
					csvOutput.write(tiempos[i].getTiempoInicialS());
					csvOutput.write(tiempos[i].getTiempoFinalS());
					csvOutput.write(tiempos[i].getTiempoTranscurridoS());
					csvOutput.endRecord();
				}
			}else if(tipoMedidaTomando.equals("TF")){
				//Falta hacer el header del documento
				for(int i=0;i<fallidas.length;i++){
					csvOutput.write(fallidas[i].getIdS());
					csvOutput.write(fallidas[i].getBooleanoS());
					csvOutput.endRecord();
				}
			}
			
			
			csvOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	public MedidaTiempo[] getTiempos() {
		return tiempos;
	}


	public void setTiempos(MedidaTiempo[] tiempos) {
		this.tiempos = tiempos;
	}
	
	
}
