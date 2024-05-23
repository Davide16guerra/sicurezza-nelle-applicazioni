package Registration;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Part;

import Utility.Database;
import Utility.Password;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
class RegistrationDao {

	protected static boolean isPasswordsEquals(byte[] pass, byte[] confirmPass) {

		if (Arrays.equals(pass, confirmPass)) {
			return true;
		} else {
			return false;
		}
	}

	protected static boolean verifyPasswordFormat(byte[] passwordBytes) {
		// conversione password in stringa
		String password = new String(passwordBytes, StandardCharsets.US_ASCII);

		if (password.length() < 8) {
			return false;
		}

		// Almeno una maiuscola
		if (!Pattern.compile("[A-Z]").matcher(password).find()) {
			return false;
		}

		// Almeno una minuscola
		if (!Pattern.compile("[a-z]").matcher(password).find()) {
			return false;
		}

		// Almeno un numero
		if (!Pattern.compile("[0-9]").matcher(password).find()) {
			return false;
		}

		// Almeno un carattere speciale (non causante SQL Injection)
		if (!Pattern.compile("[!@#$%^&*()-_+{}':,.<>?/]").matcher(password).find()) {
			return false;
		}

		return true;
	}

	protected static boolean verifyUsernameFormat(String username) {
		String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(username);

		return matcher.matches();
	}

	protected static void loadCredentialIntoDB(String name, byte[] pass, Part fileImage) {
		try {
			// connessione al DB
			Connection con = Database.connect("user_write", "Write_user");

			// generazione del sale
			byte[] salt = Password.generateSalt();
			// applica SHA-256(pass+sale)
			byte[] hashed_password = Password.hashPassword(pass, salt);

			// Verifica la dimensione del file prima di procedere per evitare TOCTOU
			long size = fileImage.getSize();
			if (size != RegistrationServlet.goodSize) {
				System.out.println("File has wrong size");
				throw new IllegalArgumentException("Messaggio dell'eccezione");
			} else {
				System.out.println("File has good size");

				// oggetto prepared statement che consente di eseguire una query al db...
				String quary = "INSERT INTO users (username, password, file_image) VALUES(?, ?, ?)";
				PreparedStatement ps = con.prepareStatement(quary);
				ps.setString(1, name);
				ps.setBytes(2, hashed_password);
				ps.setBinaryStream(3, fileImage.getInputStream());
				// svuoto la password
				Arrays.fill(pass, (byte) 0);
				Arrays.fill(hashed_password, (byte) 0);
				// esegue effettivamente la query ed ottiene un oggetto ResultSet che contiene
				// la risposta del db
				ps.executeUpdate();

				// aggiunta del sale nel DB
				quary = "INSERT INTO salts (username, salt) VALUES(?, ?);";
				ps = con.prepareStatement(quary);
				ps.setString(1, name);
				ps.setBytes(2, salt);
				// svuoto il sale
				Arrays.fill(salt, (byte) 0);
				// esegue effettivamente la query ed ottiene un oggetto ResultSet che contiene
				// la risposta del db
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
