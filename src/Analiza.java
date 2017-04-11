import java.io.IOException;
import java.util.Date;

public class Analiza {

	public static void main(String[] args) {

		Date d1 = new Date();

		// El numero de hilos a utilizar
		int nProductores = 6;
		// Declaramos una variable de tipo array de referencias a Thread
		Thread[] analizadores = new Productor[nProductores];

		// Creamos el buffer con un un tamaño y pasando el numero de
		// hilos
		Almacen al = new Almacen(15, nProductores);

		Consumidor c1 = new Consumidor(al);

		// Para el tratamiento de errores de Entrada/Salida
		try {

			// AccesoFichero es una clase que tiene el acceso sincronizado
			// al fichero de forma que si uno esta leyendo una linea otro
			// pueda leer hasta que el anterior no finalice.

			AccesoFichero af = new AccesoFichero("urls.txt");

			// Se lanza el hilo consumidor
			c1.start();

			// Se crean y lanzan los productores
			for (int i = 0; i < nProductores; i++) {
				analizadores[i] = new Productor(al, af);
				analizadores[i].start();
			}

			// Indicamos que se debe esperar a que todos los hilos
			// productores hayan finalizado
			try {
				for (int i = 0; i < nProductores; i++) {
					analizadores[i].join();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Una vez aqui todos los productores habran finalizado
			// asi que se puede cerrar el acceso al fichero.

			af.cierraFichero();
			Date d2 = new Date();

			// Para mostrar el tiempo de ejecucion.
			System.out.println("Tiempo en milisegundos " + (d2.getTime() - d1.getTime()));
		} catch (IOException e) {
			System.out.println("Error leyendo el fichero de direcciones");
		}
	}
}