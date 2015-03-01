package nicolas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Principal {
	
	private static int servidores,clientes,consultas;
	
	public static void main(String[] args) {
		
		servidores=0;
		clientes=0;
		consultas=0;
		
		lectura("data/in");
		
		System.out.println("El numero de servidores es: "+servidores);
		System.out.println("El numero de clientes es: "+clientes);
		System.out.println("El numero de consultas es: "+consultas);
		
		Buffer buffer=new Buffer(clientes,6);
		
		for(int i=0;i<servidores;i++){
			Thread thread=new Thread(new Servidor(i, buffer));
			thread.start();
		}
		
		for(int i=0;i<clientes;i++){
			Thread thread=new Thread(new Cliente(i, consultas, buffer));
			thread.start(); 
		}
		
	}
	
	
	private static void lectura(String ruta){
		BufferedReader reader = null;
		try{
			File file = new File(ruta);
			reader = new BufferedReader(new FileReader(file));
			
			String line;
			servidores=Integer.parseInt(reader.readLine().split(":")[1]);
			clientes=Integer.parseInt(reader.readLine().split(":")[1]);
			consultas=Integer.parseInt(reader.readLine().split(":")[1]);
			
//			while((line = reader.readLine())!=null){
//				System.out.println(line);
//			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
