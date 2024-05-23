package Home;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

import Utility.Database;
import Utility.Proposal;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class HomeDao {
	@GuardedBy("username")
	public static String username;

	public HomeDao(String username) {
		HomeDao.username = username;
	}

	protected static void loadFileIntoDB(Part fileContent, String fileName)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		// connessione al DB
		Connection con = Database.connect("user_update", "Update_user");

		// Verifica la dimensione del file prima di procedere per evitare TOCTOU
		long size = fileContent.getSize();
		if (size != HomeServlet.goodSize) {
			System.out.println("File has wrong size");
		} else {
			System.out.println("File has good size");

			// oggetto prepared statement che consente di eseguire una query al db...
			String quary = "UPDATE users SET file = ?, file_name = ? WHERE username = ?;";
			PreparedStatement ps = con.prepareStatement(quary);
			ps.setBinaryStream(1, fileContent.getInputStream());
			ps.setString(2, fileName);
			ps.setString(3, username);

			// esegue effettivamente la query ed ottiene un oggetto ResultSet che contiene
			// la risposta del db
			ps.executeUpdate();
		}
	}

	@SuppressWarnings("deprecation")
	public byte[] getProfileImage()
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		String url = "jdbc:mysql://localhost:3306/sa_project";
		Connection con = DriverManager.getConnection(url, "user_read", "Read_user");
		PreparedStatement ps = con.prepareStatement("SELECT file_image FROM users WHERE username = ?");
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getBytes("file_image");
	}

	@SuppressWarnings("deprecation")
	public List<Proposal> getProposalsList() {
		List<Proposal> proposalsList = new ArrayList<Proposal>();
		try {
			// inizializza il driver per comunicare con il db
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			// stringa di connessione: indirizzo - porta - nome db
			String url = "jdbc:mysql://localhost:3306/sa_project";
			// oggetto connessione al db tramite inserimento di credenziali: stringa di
			// connessione - nome utente - password
			Connection con = DriverManager.getConnection(url, "user_read", "Read_user");
			// oggetto prepared statement che consente di eseguire una query al db...
			PreparedStatement ps = con.prepareStatement("SELECT username, file_name, file FROM users");
			// estrazione del risultato
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String username = rs.getString("username");
				String fileName = rs.getString("file_name");
				byte[] fileContent = rs.getBytes("file");

				if (fileName != null) {
					Proposal proposal = new Proposal(username, fileName, fileContent);
					proposalsList.add(proposal);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return proposalsList;

	}
}
