
public class Contador {
	private long cont;

	/**
	 * Constructor de Contador Pone el contador a cero
	 */
	public Contador() {
		cont = 0;
	}

	/**
	 * Metodo getContador
	 * 
	 * @return el valor del contador
	 */
	public long getContador() {
		return cont;
	}

	/**
	 * Aumenta una cantidad el valor del contador
	 * 
	 * @param cantidad
	 *            lo que se suma al contador
	 */
	public void aumentaContador(long cantidad) {
		cont = cont + cantidad;
	}

}
