import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AccesoFichero {
	private BufferedReader br;

	public AccesoFichero(String fich) throws IOException {
		br = new BufferedReader(new FileReader(fich));
	}

	public synchronized String siguienteDireccion() throws IOException {
		return br.readLine();
	}

	public void cierraFichero() throws IOException {
		br.close();
	}

}
