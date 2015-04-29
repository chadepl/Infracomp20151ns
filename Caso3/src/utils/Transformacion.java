/**
 * 
 */
package utils;


/**
 * Clase que contiene metodos static que proveen de metodos de transformacion al protocolo.
 * Infraestructura Computacional 201320
 * Universidad de los Andes.
 * Las tildes han sido eliminadas por cuestiones de compatibilidad.
 * @author Michael Andres Carrillo Pinzon
 */
public class Transformacion {

	/**
	 * Separador de los bytes.
	 */
	public static final String SEPARADOR2 = ";";
	
	/**
	 * Algoritmo de encapsulamiento a enteros. Convierte los bytes de un String a su representacion como enteros.
	 * @param b Los bytes a representar como enteros.
	 * @return EL string construido con la representacion de bytes como enteros.
	 */
	public static String codificar( byte[] b )
	{
		// Encapulacion sobre enteros separados por ;
		/*String s=null;
		try {
			s = new String("".getBytes(),ThreadServidor.COD);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < b.length; i++) 
		{
			if( i == b.length-1 )
				s += Byte.toString(b[i]);
			else
				s += Byte.toString(b[i])+SEPARADOR2;
		}
		return s;*/
		
		// Encapsulamiento con hexadesimales
		String ret = "";
		for (int i = 0 ; i < b.length ; i++) {
			String g = Integer.toHexString(((char)b[i])&0x00ff);
			ret += (g.length()==1?"0":"") + g;
		}
		return ret;
	}

	/**
	 * Algoritmo que transforma los enteros en los bytes correspondientes.
	 * @param ss El string con los enteros a transformar.
	 * @return Los bytes en su representacion real.
	 */
	public static byte[] decodificar( String ss)
	{
		// Encapulacion sobre enteros separados por ;
		/*String[] s = ss.split(SEPARADOR2);
		byte[] b = new byte[s.length];

		for (int i = 0; i < b.length; i++) 
		{
			try {
				b[i] = Byte.parseByte(s[i],10);
			} catch (Exception e) {
				System.out.println(ss+":"+e.getMessage());
			} 
		}
		return b;*/
		
		// Encapsulamiento con hexadesimales
		byte[] ret = new byte[ss.length()/2];
		for (int i = 0 ; i < ret.length ; i++) {
			ret[i] = (byte) Integer.parseInt(ss.substring(i*2,(i+1)*2), 16);
		}
		return ret;
	}
}
