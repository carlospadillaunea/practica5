import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Hilos Productor. Lee de un fichero las direcciones URL a las que se debe
 * conectar. Lee el contenido y almacena las palabras que encuentran junto con
 * el número de apariciones de cada una de ellas. Los pares palabra/contador se
 * almacenan en un HashMap y este se almacena en una determinada posicion de un
 * almacen.
 * 
 */
public class Productor extends Thread {
	private Almacen almacen;
	private String dirURL;
	private AccesoFichero br;
	private int pos;

	/**
	 * Constructor de Productor
	 * 
	 * @param c
	 *            Almacen donde se guardara el HashMap
	 * @param buf
	 *            referencia al objeto AccesoFichero que contiene las pag. web.
	 */
	public Productor(Almacen c, AccesoFichero buf) {
		almacen = c;
		br = buf;
	}

	/**
	 * Sobreescribe el metodo run de Thread Lee del fichero una direccion web a
	 * analizar. Extrae las palabras y cuenta el numero de apariciones de cada
	 * una de ellas. Almacena el resultado en un HashMap y este en el buffer. Si
	 * no es final de fichero procede con la siguiente direccion disponible.
	 */
	public void run() {
		try {
			while ((dirURL = br.siguienteDireccion()) != null) {
				HashMap hm = new HashMap();
				System.out.println("Analizando: " + dirURL);

				try {
					URL direccion = new URL(dirURL);

					// Se obtiene un InputStream para leer
					InputStream direccionIn = direccion.openConnection().getInputStream();

					// Lo envolvemos en un BufferedReader para leer lineas
					BufferedReader fromURL = new BufferedReader(new InputStreamReader(direccionIn));

					String linea;
					String cadena;
					Contador contador;

					// Mientras quede informacion para leer
					while ((linea = fromURL.readLine()) != null) {

						// Delimitadores
						StringTokenizer st = new StringTokenizer(linea, ",.;:¡¿!?<>\"='-/#$*[]{}()%+@?_ ");
						while (st.hasMoreTokens()) {
							cadena = st.nextToken().toLowerCase();
							// Si la palabra no esta, la añadimos
							if (!hm.containsKey(cadena)) {
								Contador c = new Contador();
								c.aumentaContador(1);
								hm.put(cadena, c);
							} else {
								// Si esta aumentamos el contador
								((Contador) hm.get(cadena)).aumentaContador(1);
							}

						}
					}
					fromURL.close();
					// Guardamos el resultado
					almacen.almacena(hm);
					System.out.println("Finaliza analisis de " + dirURL);
				} catch (MalformedURLException e) {
					System.out.println("Direccion URL no valida" + dirURL);
					e.printStackTrace();
					// Informamos de que este hilo ha fallado y no guardara el
					// resultado
				} catch (IOException e) {
					System.out.println("Imposible leer de " + dirURL);
					e.printStackTrace();
					// Informamos de que este hilo ha fallado y no guardara el
					// resultado
				} catch (Exception e) {
					System.out.println("Direccion URL no valida" + dirURL);
					e.printStackTrace();
					// Informamos de que este hilo ha fallado y no guardara el
					// resultado
				}
			}
		} catch (IOException e) {
			System.out.println("Error con el fichero (en un productor)");
		}
		almacen.notificarTerminado();

	}
}
