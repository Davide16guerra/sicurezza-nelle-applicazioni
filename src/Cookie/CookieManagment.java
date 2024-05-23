package Cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class CookieManagment {

	public static boolean setCookie(String token, HttpServletResponse response) {
		// Crea il cookie "rememberme" e imposta i flag necessari
		Cookie rememberMeCookie = new Cookie("rememberme", token);
		rememberMeCookie.setMaxAge(7 * 24 * 60 * 60); // Cookie valido per una settimana
		rememberMeCookie.setSecure(true);
		rememberMeCookie.setHttpOnly(true);

		// Aggiungi il cookie alla risposta HTTP
		response.addCookie(rememberMeCookie);
		return true;
	}

}
