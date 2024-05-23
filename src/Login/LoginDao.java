package Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

import Utility.Password;
import net.jcip.annotations.ThreadSafe;
import Utility.Database;

@ThreadSafe
public class LoginDao {

	public static boolean isUserValid(String name, byte[] pass) {
		boolean status = false;
		try {
			// connessione al DB
			Connection con = Database.connect("user_read", "Read_user");
			// oggetto prepared statement che consente di eseguire una query al db...
			PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
			// imposto l'username
			ps.setString(1, name);

			// prendo il sale dal db
			PreparedStatement ps2 = con.prepareStatement("SELECT salt FROM salts WHERE username = ?");
			ps2.setString(1, name);
			ResultSet rs = ps2.executeQuery();
			rs.next();
			byte[] salt = rs.getBytes("salt");

			// applico SHA-256 alla password+sale
			byte[] hashed_password = Password.hashPassword(pass, salt);
			// imposto la password date in input dall'utente alla jsp, dalla jsp alla
			// servlet e dalla servlet al DAO
			ps.setBytes(2, hashed_password);
			// svuoto la password e il sale
			Arrays.fill(pass, (byte) 0);
			Arrays.fill(salt, (byte) 0);
			// esegue effettivamente la query ed ottiene un oggetto ResultSet che contiene
			// la risposta del db
			rs = ps.executeQuery();
			// il next() prende la prima riga del risultato della query
			// restituisce true se c'è almeno una riga altrimenti false
			status = rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return status;

	}
}
