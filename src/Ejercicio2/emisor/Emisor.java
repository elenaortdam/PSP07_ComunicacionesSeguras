package Ejercicio2.emisor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class Emisor {

	public void signDocument() throws SignatureException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException {

		String documentPath = System.getProperty("user.dir");

		if (!documentPath.endsWith(File.pathSeparator)) {
			documentPath += File.separator + "Ejercicio2" + File.separator;
		}

		String privateKeyPath = documentPath + "private.key";

		//Lectura del fichero de clave privada
		byte[] bufferPriv = Files.readAllBytes(Paths.get(privateKeyPath));

		//Recupera la clave privada codificada en formato PKCS8
		PKCS8EncodedKeySpec clavePrivadaSpec = new PKCS8EncodedKeySpec(bufferPriv);
		KeyFactory keyDSA = KeyFactory.getInstance("DSA");
		PrivateKey privateKey = keyDSA.generatePrivate(clavePrivadaSpec);
		//Firma con la clave privada
		Signature signature = Signature.getInstance("SHA256withDSA");
		signature.initSign(privateKey);

		String exampleFile = documentPath + "EjemploFichero.docx";

		byte[] mensaje = Files.readAllBytes(Paths.get(exampleFile));
		signature.update(mensaje);

		//Firmamos el mensaje
		byte[] sign = signature.sign();

		//Guardo la firma en otro fichero
		String signedFile = documentPath + "SIGNED.FILE";
		FileOutputStream fos = new FileOutputStream(signedFile);
		fos.write(sign);
		fos.close();
	}

	public static void main(String[] args)
			throws IOException, SignatureException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {

		KeyGenerator generator = new KeyGenerator();
		generator.generateSecureKey();
		Emisor emisor = new Emisor();
		emisor.signDocument();
	}
}
