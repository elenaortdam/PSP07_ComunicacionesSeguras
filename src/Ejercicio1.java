import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Ejercicio1 {

	private String filePath;

	private void generateFileSummary(String algorithm) {

		JFileChooser jFileChooser = new JFileChooser();
		File selectedFile;
		int tries = 0;
		//Solo mostramos JFileChooser la primera vez como máximo 3 veces
		if (this.filePath == null) {
			do {
				jFileChooser.showSaveDialog(null);
				selectedFile = jFileChooser.getSelectedFile();
				tries++;
			} while (selectedFile == null && tries < 3);
			//Si en las 3 veces no escoge ningún archivo finalizamos el programa
			if (selectedFile == null) {
				System.out.println("No se ha escogido ningún fichero. No se puede generar el resumen");
				System.exit(-1);
			}
			this.filePath = selectedFile.getAbsolutePath();

		}
		String hashGenerated = "";
		try {
			hashGenerated = generateHash(algorithm, filePath).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			System.out.printf("El algoritmo %s no existe...", algorithm);
			System.exit(-1);
		}
		System.out.println("========================================================================");
		System.out.printf("Fichero a resumir con %s: %s \n", algorithm, filePath);
		System.out.printf("Mensaje en Hexadecimal: %s\n", hashGenerated);
	}

	public static String generateHash(String algorithm, String path)
			throws NoSuchAlgorithmException {

		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

		InputStream file = null;
		try {
			file = new FileInputStream(path);
		} catch (IOException e) {
			System.out.printf("Fallo al encontrar el fichero en la ruta: %s\n", path);
			System.exit(-1);
		}

		byte[] buffer = new byte[1];
		int endFile = -1;
		int character;

		try {

			character = file.read(buffer);
			while (character != endFile) {
				messageDigest.update(buffer);
				character = file.read(buffer);
			}
			//cerramos el archivo
			file.close();
		} catch (IOException e) {
			System.out.println("Fallo al leer el fichero");
			System.exit(-1);
		}
		byte[] summaryBytes = messageDigest.digest(); // Genera el resumen

		//Pasamos los resumenes a hexadecimal
		StringBuilder hash = new StringBuilder();
		for (byte summary : summaryBytes) {
			hash.append(Integer.toHexString((summary >> 4) & 0xf));
			hash.append(Integer.toHexString(summary & 0xf));
		}
		return hash.toString();
	}

	public static void main(String[] args) {
		Ejercicio1 ejercicio1 = new Ejercicio1();
		ejercicio1.generateFileSummary("SHA-256");
		ejercicio1.generateFileSummary("MD5");
	}

}
