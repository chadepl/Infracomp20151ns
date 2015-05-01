package generadorCarga;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.csvreader.CsvWriter;

public class Generator {

	///////////////////
	//ESCENARIO 1//////
	///////////////////
	private static final int NUM_TRANSACCIONES1=400;
	private static final int GAP_TRANSACCION1=20;
	
	///////////////////
	//ESCENARIO 2//////
	///////////////////
	private static final int NUM_TRANSACCIONES2=200;
	private static final int GAP_TRANSACCION2=40;
	
	///////////////////
	//ESCENARIO 3//////
	///////////////////
	private static final int NUM_TRANSACCIONES3=80;
	private static final int GAP_TRANSACCION3=100;
	
	private LoadGenerator generator;
	private static ArrayList<Medida> medidas;
	private static int numeroTransaccionesPerdidas;
	
	private static int i=0;

	public Generator(int escenario){
		
		int numberOfTasks=0;
		int gapBetweenTasks=0;

		medidas = new ArrayList();
		numeroTransaccionesPerdidas = 0;
		
		if(escenario==1){
			numberOfTasks=NUM_TRANSACCIONES1;
			gapBetweenTasks=GAP_TRANSACCION1;
		}else if(escenario==2){
			numberOfTasks=NUM_TRANSACCIONES2;
			gapBetweenTasks=GAP_TRANSACCION2;
		}else if(escenario==3){
			numberOfTasks=NUM_TRANSACCIONES3;
			gapBetweenTasks=GAP_TRANSACCION3;
		}
		Task work=createTask();
		generator=new LoadGenerator("Client - Server load test", numberOfTasks, work, gapBetweenTasks);
		generator.generate();
		System.out.println("Acabo de generar");
	}

	public static synchronized void guardarInformacion(long nTiempoAutenticacion, long nTiempoFin, String idCliente) {

		Medida medida = new Medida(nTiempoAutenticacion,nTiempoFin,idCliente);
		medidas.add(medida);
		
		String outputFile="";
		String rutaBase="data/";
		
		//El numero es el escenari o
		
		outputFile=rutaBase+"tiempos"+1+".csv";
			
		
		// before we open the file check to see if it already exists
		boolean alreadyExists = new File(outputFile).exists();
			
		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
			
			// if the file didn't already exist then we need to write out the header line
			if (!alreadyExists)
			{
				csvOutput.write("idCliente");
				csvOutput.write("tiempoAutenticacion");
				csvOutput.write("tiempoTotal");
				csvOutput.write("transaccionesPerdidas");
				csvOutput.endRecord();
			}
			// else assume that the file already has the correct header line
				
					csvOutput.write(medida.getIdCliente());
					csvOutput.write((medida.getTiempoAutenticacion()+""));
					csvOutput.write((medida.getTiempoTotal()+""));
					if(i==79){
						csvOutput.write((numeroTransaccionesPerdidas+""));
					}else{
						csvOutput.write((0+""));
					}
					csvOutput.endRecord();
				
			
			csvOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static synchronized void aumentarNumeroTransaccionesPerdidas() {

		numeroTransaccionesPerdidas++;
	}

	private Task createTask() {
		// TODO Auto-generated method stub
		return new ClientServerTask();
		//return null;
	}
	
	public static void main(String[] args) {
		Generator generator=new Generator(3);
		//generarReporte(1);
	}
	
	public static void generarReporte(int escenario){
		
		String outputFile="";
		String rutaBase="data/";
		
			if(escenario>=1 && escenario<=12){
				outputFile=rutaBase+"tiempos"+escenario+".csv";
			}else{
				System.out.println("ERROR");
			}
		
		// before we open the file check to see if it already exists
		boolean alreadyExists = new File(outputFile).exists();
			
		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
			
			// if the file didn't already exist then we need to write out the header line
			if (!alreadyExists)
			{
				csvOutput.write("idCliente");
				csvOutput.write("tiempoAutenticacion");
				csvOutput.write("tiempoTotal");
				csvOutput.endRecord();
			}
			// else assume that the file already has the correct header line
			
			
				for(int i=0;i<medidas.size();i++){
					csvOutput.write(medidas.get(i).getIdCliente());
					csvOutput.write((medidas.get(i).getTiempoAutenticacion()+""));
					csvOutput.write((medidas.get(i).getTiempoTotal()+""));
					csvOutput.endRecord();
				}
			
			csvOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
