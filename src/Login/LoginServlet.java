package Login;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Cookie.CookieManagment;
import Cookie.Token;
import net.jcip.annotations.ThreadSafe;

@WebServlet("/LoginServlet")
@ThreadSafe
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// prende dal client username e password
		String username = request.getParameter("username");
		byte[] password = request.getParameter("pass").getBytes();
		String rememberMe = request.getParameter("rememberMe");

		try {
			// gestione remember me
			System.out.println("Remember me: " + rememberMe);
			if (rememberMe != null) {
				// generazione del token
				String token = Token.generateToken(username, password);
				// genera il cookie
				boolean cookie = CookieManagment.setCookie(token, response);
				System.out.println("Generated cookie: " + cookie);
			}

			// verifica se nome utente e password sono presenti nel db
			if (LoginDao.isUserValid(username, password)) {
				System.out.println("login done\n");
				// assegna l'username alla home
				request.setAttribute("username", username);
				// Reindirizza alla Home.jsp
				RequestDispatcher dispatcher = request.getRequestDispatcher("Home.jsp");
				dispatcher.forward(request, response);

			} else {
				response.sendRedirect("LoginError.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
