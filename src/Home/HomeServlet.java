package Home;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.Part;

import Utility.File;
import net.jcip.annotations.ThreadSafe;

@WebServlet("/HomeServlet")
@MultipartConfig
@ThreadSafe
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// dimensione massima del file .txt (1mb)
	protected static long goodSize;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// prendi il file dal client
			Part filePart = request.getPart("file");
			// estrai il path dal file
			String filePath = File.getFilePath(filePart);

			// memorizzo la dimensione del file
			goodSize = filePart.getSize();

			// verifica il tipo del file
			String fileType = File.fileType(filePath);
			if (fileType.contains("text/plain;")) {
				// verifico se il file contiene script
				if (File.containsScript(filePart) == false) {
					System.out.println("Valid file");
					// estrai il nome dal path
					String fileName = File.getFileNameToPath(filePath);
					System.out.println("file name: " + fileName);
					// memorizza il file nel db
					HomeDao.loadFileIntoDB(filePart, fileName);
					System.out.println("file uploaded\n");
					// feedback della home
					request.setAttribute("fileAccepted", true);
				} else {
					System.out.println("WARNING: file contains script");
					System.out.println("Unvalid file\n");
					// feedback della home
					request.setAttribute("fileAccepted", false);
				}
			} else {
				System.out.println("Unvalid file type\n");
				// feedback della home
				request.setAttribute("fileAccepted", false);
			}

			// assegna l'username alla home
			request.setAttribute("username", HomeDao.username);
			// Reindirizza alla Home.jsp
			RequestDispatcher dispatcher = request.getRequestDispatcher("Home.jsp");
			dispatcher.forward(request, response);

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
