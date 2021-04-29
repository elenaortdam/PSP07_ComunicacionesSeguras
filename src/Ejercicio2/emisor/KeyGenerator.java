package Ejercicio2.emisor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyGenerator {

	public void generateSecureKey() {

		String documentPath = System.getProperty("user.dir");

		if (!documentPath.endsWith(File.pathSeparator)) {
			documentPath += File.separator + "/Ejercicio2" + File.separator;
		}

		KeyPair keys = null;
		try {
			//Creamos el par de claves usando el algoritmo DSA
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
			//Inicializamos el generador de claves
			SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
			keyGen.initialize(2048, number);

			keys = keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error: " + e.getLocalizedMessage() + ". Cerrando el programa...");
			System.exit(-1);
			e.printStackTrace();
		}

		try {
			//Almacenamos la clave en un fichero
			saveKeys(documentPath, keys);
			System.out.println("Guardadas claves para firma del fichero");
		} catch (IOException e) {
			System.out.println("Error: Fallo al crear la clave. Cerrando el programa...");
			System.exit(-1);
		}

	}

	private void saveKeys(String documentPath, KeyPair keys) throws IOException {

		//Codificamos la clave privada en formato pk8spec
		PrivateKey privateKey = keys.getPrivate();
		PKCS8EncodedKeySpec pk8spec = new PKCS8EncodedKeySpec(privateKey.getEncoded());

		//Escribimos la clave privada en el fichero
		String privateKeyPath = documentPath + "private.key";
		FileOutputStream outPrivateKey = new FileOutputStream(privateKeyPath);
		outPrivateKey.write(pk8spec.getEncoded());
		outPrivateKey.close();

		PublicKey publicKey = keys.getPublic();
		X509EncodedKeySpec pkx509 = new X509EncodedKeySpec(publicKey.getEncoded());
		String publicKeyPath = documentPath + "public.key";
		FileOutputStream outPublicKey = new FileOutputStream(publicKeyPath);
		outPublicKey.write(pkx509.getEncoded());
		outPublicKey.close();

	}
}
