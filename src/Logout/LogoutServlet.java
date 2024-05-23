package Logout;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.jcip.annotations.ThreadSafe;

@WebServlet("/LogoutServlet")
@ThreadSafe
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// invalida la sessione https
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // Invalida la sessione
		}

		// Rimuovi il cookie dal browser
		Cookie cookie = new Cookie("rememberme", null);
		cookie.setMaxAge(0); // Imposta il timeout a 0 per rimuovere il cookie
		response.addCookie(cookie);

		System.out.print("\nLogout done\n");
		response.sendRedirect("SessionExpired.jsp"); // Redirect alla pagina di login
	}

}
