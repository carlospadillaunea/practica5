import java.util.HashMap;

/**
 * Clase Buffer: Implementa un buffer de lectura/escritura con estructura cola
 * circular El tamaño no influye en la utilización, sino en el rendimiento
 * 
 * @version 1.0
 */
class Almacen {
	private int hilosActivos;
	private int max, ini, fin;
	private HashMap[] info;

	/**
	 * Constructor de la clase
	 * 
	 * @param tam
	 *            Indica el tamaño en memoria del Buffer
	 */
	public Almacen(int tam, int nhilos) {
		ini = 0;
		fin = 0;
		max = tam;
		hilosActivos = nhilos;
		info = new HashMap[max];
	}

	/**
	 * Inserta un elemento en el Buffer.
	 * 
	 * @param elemento
	 *            Representa el elemento a introducir en el Buffer
	 * @param posicion
	 *            posicion en la que se debe insertar la informacion
	 */
	public synchronized void almacena(HashMap elemento) {
		if (((fin + 1) % max) == ini)
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		System.out.println("Inserto en " + fin);
		info[fin] = elemento;
		fin = (fin + 1) % max;
		notifyAll();
	}

	/**
	 * Extrae un elemento del Buffer. Si el Buffer está vacío, el hilo que lo
	 * llama se espera hasta que haya elementos para extraer.
	 * 
	 * @return Elemento extraído
	 */
	public synchronized HashMap extrae() {
		HashMap hm = null;
		try {
			while ((info[ini] == null) && (hilosActivos > 0)) // Buffer vacío y
																// hilos
																// productores
																// vivos
			{
				wait();
			}
			// End while
			System.out.println("Extraigo de " + ini);
			hm = info[ini];
			info[ini] = null;
			ini = (ini + 1) % max;

		} catch (InterruptedException e) {
			System.out.println("Error en la sincronización");
			e.printStackTrace(System.out);
		}

		notifyAll();
		return hm;
	}

	public synchronized void notificarTerminado() {
		hilosActivos--;
		notifyAll();
	}

	public synchronized boolean finTodos() {
		if (hilosActivos > 0)
			return false;
		else
			return true;
	}
}
