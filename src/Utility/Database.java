package Utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import net.jcip.annotations.ThreadSafe;

import java.io.File;

@ThreadSafe
public class Database {
	/**
	 * Metodo di connessione al db "my_db"
	 * 
	 * @return true sse connesso false altrimenti
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */

	public static Connection connect(String username, String password)
			throws ClassNotFoundException, SQLException, IOException {

		// Carica il file di configurazione
		Properties prop = new Properties();
		InputStream input = new FileInputStream(
				new File("C:\\Users\\david\\eclipse-workspace\\progetto_SA\\config.ini"));
		prop.load(input);

		// Ottieni i valori dalle proprietà del file di configurazione
		String src = prop.getProperty("source");
		String port = prop.getProperty("port");
		String db = prop.getProperty("schema_name");
		String timezone = prop.getProperty("connection_parameters");

		try {
			// inizializza il driver per comunicare con il db
			Class.forName("com.mysql.cj.jdbc.Driver");
			// stringa di connessione: indirizzo - porta - nome db
			String jdbc = (new StringBuilder("jdbc:mysql://")).append(src).append(":").append(port).append("/")
					.append(db).append(timezone).toString();
			// oggetto connessione al db tramite inserimento di credenziali: stringa di
			// connessione - nome utente - password
			Connection connection = DriverManager.getConnection(jdbc, username, password);
			return connection;

		} catch (SQLException e) {
			if (e.getErrorCode() == 1045) {
				System.out.println(
						"Incorrect DB username or password! \n Please check your configuration file and try again" + e);
			} else {
				System.out.println("Error connecting to database!");
			}
		} catch (Exception e1) {
			System.out.println("Error connecting to database!" + e1.getMessage());
		}
		return null;
	}

}
