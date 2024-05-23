package Utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class Password {

	public static byte[] hashPassword(byte[] password, byte[] salt) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// Aggiunge il salt alla password e calcola l'hash
			digest.update(salt);
			byte[] hashedPassword = digest.digest(password);

			return hashedPassword;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] generateSalt() {
		try {
			// Utilizza SecureRandom per ottenere numeri casuali sicuri
			SecureRandom secureRandom = SecureRandom.getInstanceStrong();

			// Genera un array di byte per il salt
			byte[] salt = new byte[16];
			secureRandom.nextBytes(salt);

			return salt;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}

}
