package Registration;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import Utility.File;
import net.jcip.annotations.ThreadSafe;

@WebServlet("/RegistrationServlet")
@MultipartConfig
@ThreadSafe
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// dimensione massima del file .txt (1mb)
	protected static long goodSize;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			// prendo dal client username, password, confirm password
			String username = request.getParameter("username");
			byte[] password = request.getParameter("pass").getBytes();
			byte[] confirm_password = request.getParameter("confirmPass").getBytes();
			// prendo dal client l'immagie del profilo
			Part filePart = request.getPart("file_image");
			// prendo il path dell'immagine
			String fileImagePath = File.getFilePath(filePart);

			// memorizzo la dimensione del file
			goodSize = filePart.getSize();

			// verifica del tipo del file
			String fileType = File.fileType(fileImagePath);
			if ("image/jpeg".equals(fileType) || "image/png".equals(fileType)) {
				System.out.println("Valid file");
				// verifica il formato dell'username
				if (RegistrationDao.verifyUsernameFormat(username)) {
					// verifica se le password coincidono
					if (RegistrationDao.isPasswordsEquals(password, confirm_password)) {
						// verifica il formato della password
						if (RegistrationDao.verifyPasswordFormat(password)) {
							// carica i dati nel db
							RegistrationDao.loadCredentialIntoDB(username, password, filePart);
							System.out.println("registration done\n");
							response.sendRedirect("registrationOK.jsp");
						} else {
							System.out.print("incorrect password format\n");
							response.sendRedirect("registrationKO_passwordFormat.jsp");
						}
					} else {
						System.out.print("incorrect confirm password\n");
						response.sendRedirect("registrationKO_confirmPassword.jsp");
					}
				} else {
					System.out.print("incorrect username\n");
					response.sendRedirect("registrationKO_usernameFormat.jsp");
				}

			} else {
				System.out.print("Unvalid file type\n");
				response.sendRedirect("RegistrationKO_fileType.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("RegistrationKO_fileType.jsp");
		}
	}
}
