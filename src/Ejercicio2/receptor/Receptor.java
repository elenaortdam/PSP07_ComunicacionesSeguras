package Ejercicio2.receptor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class Receptor {

	private void readSignedMessage() {

		String publicSignPath = System.getProperty("user.dir");
		if (!publicSignPath.endsWith(File.pathSeparator)) {
			publicSignPath += File.separator + "Ejercicio2" + File.separator;
		}

		try {
			String publicKeyPath = publicSignPath + "public.key";

			//Lectura del fichero de la clave pública
			byte[] publicBuffer = new byte[0];
			try {
				publicBuffer = Files.readAllBytes(Paths.get(publicKeyPath));
			} catch (NoSuchFileException e) {
				System.out.printf("Error al encontrar el fichero de la clave pública en la ruta: %s\n",
								  publicKeyPath);
				System.exit(-1);

			}

			KeyFactory keyFactory = KeyFactory.getInstance("DSA");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicBuffer);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

			//Fichero firma
			String signFilePath = publicSignPath + "SIGNED.FILE";
			byte[] signBuffer = new byte[0];
			try {
				signBuffer = Files.readAllBytes(Paths.get(signFilePath));
			} catch (NoSuchFileException e) {
				System.out.printf("Error al encontrar el fichero de la firma en la ruta: %s\n",
								  signFilePath);
				System.exit(-1);
			}

			//Inicializamos el objeto signature con clave publica para verificar
			Signature signature = Signature.getInstance("SHA256withDSA");
			signature.initVerify(publicKey);

			//Leemos el fichero que queremos verificar
			String exampleFile = publicSignPath + "EjemploFichero.docx";
			byte[] message = new byte[0];
			try {
				message = Files.readAllBytes(Paths.get(exampleFile));
			} catch (NoSuchFileException e) {
				System.out.printf("Error al encontrar el fichero a verificar en la ruta: %s\n",
								  exampleFile);
				System.exit(-1);
			}
			signature.update(message);
			boolean check = signature.verify(signBuffer);

			if (check) {
				System.out.println("Firma verificada con clave pública");
			} else {
				System.out.println("Firma no verificada");
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	public static void main(String[] args) {
		Receptor receptor = new Receptor();
		receptor.readSignedMessage();
	}
}
