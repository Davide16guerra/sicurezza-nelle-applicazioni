package Cookie;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class Aes {

	private static String keyPath = "C:\\Users\\david\\eclipse-workspace\\progetto_SA\\secret.key";
	protected static String tokenPath = "token.txt";

	protected static void generateAESKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128);

			SecretKey key = keyGenerator.generateKey();
			boolean keySaved = saveKeyToFile(key);
			System.out.println("saved key: " + keySaved);
		} catch (Exception e) {
			throw new RuntimeException("Errore nella generazione della chiave", e);
		}
	}

	protected static SecretKey loadKeyFromFile() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(keyPath))) {
			return (SecretKey) ois.readObject();
		} catch (FileNotFoundException e) {
			return null; // Il file non esiste, la chiave verrà creata successivamente
		} catch (Exception e) {
			throw new RuntimeException("Errore nel caricamento della chiave dal file", e);
		}
	}

	private static boolean saveKeyToFile(SecretKey key) {

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(keyPath))) {
			oos.writeObject(key);
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Errore nel salvataggio della chiave nel file", e);
		}
	}

	// Cripta una stringa con AES
	protected static String encrypt(String plainText, SecretKey secretKey) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	// Decripta una stringa con AES
	public static String decrypt(String encryptedText) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, loadKeyFromFile());

		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

		return new String(decryptedBytes, StandardCharsets.UTF_8);
	}
}
