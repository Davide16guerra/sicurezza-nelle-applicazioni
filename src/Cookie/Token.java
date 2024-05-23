package Cookie;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import javax.crypto.SecretKey;

import Login.LoginDao;
import net.jcip.annotations.NotThreadSafe;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@NotThreadSafe
public class Token {

	public static String generateToken(String username, byte[] passwordBytes) throws Exception {
		// Conversione password in stringa
		String password = new String(passwordBytes, StandardCharsets.US_ASCII);

		// Generazione stringa casuale
		SecureRandom random = new SecureRandom();
		byte[] tokenBytes = new byte[32];
		random.nextBytes(tokenBytes);

		// Lettura token da file utilizzando FileChannel e ByteBuffer
		try (RandomAccessFile file = new RandomAccessFile(Aes.tokenPath, "r");
				FileChannel channel = file.getChannel()) {
			// Calcola la dimensione del file
			long fileSize = channel.size();
			// Alloca un ByteBuffer con la dimensione del file
			ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
			// Legge i dati dal canale nel ByteBuffer
			channel.read(buffer);
			// Converte i dati letti in una stringa
			String fileToken = new String(buffer.array(), StandardCharsets.UTF_8).trim();

			// Generazione del token completo
			String token = username + "#" + password + "#" + fileToken;

			// Criptazione del token
			Aes.generateAESKey();
			SecretKey key = Aes.loadKeyFromFile();
			String encryptedToken = Aes.encrypt(token, key);

			return encryptedToken;
		}
	}

	public static boolean verifyToken(String username, String password, String token) throws IOException {
		// Verifica username e password nel db
		Boolean userValid = LoginDao.isUserValid(username, password.getBytes());

		// Legge il token da file utilizzando FileChannel e ByteBuffer
		try (RandomAccessFile file = new RandomAccessFile("token.txt", "r"); FileChannel channel = file.getChannel()) {
			// Calcola la dimensione del file
			long fileSize = channel.size();
			// Alloca un ByteBuffer con la dimensione del file
			ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
			// Legge i dati dal canale nel ByteBuffer
			channel.read(buffer);
			// Converte i dati letti in una stringa
			String fileToken = new String(buffer.array(), "UTF-8").trim();
			// Verifica il token
			return userValid && fileToken.equals(token.trim());
		}

	}

}