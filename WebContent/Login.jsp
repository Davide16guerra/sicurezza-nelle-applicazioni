<%@page import="Cookie.Token"%>
<%@page import="Cookie.Aes"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login</title>
<style>
body {
	font-family: 'Arial', sans-serif;
	background-color: #f7f7f7;
	margin: 0;
	display: flex;
	align-items: center;
	justify-content: center;
	height: 100vh;
}

form {
	background-color: #fff;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	padding: 20px;
	max-width: 400px;
	width: 100%;
	text-align: center;
}

table {
	width: 100%;
}

td {
	padding: 10px;
}

input[type="text"], input[type="password"] {
	width: calc(100% - 20px);
	padding: 10px;
	margin-bottom: 10px;
	box-sizing: border-box;
	border: 1px solid #ddd;
	border-radius: 4px;
}

input[type="submit"], button {
	background-color: #4caf50;
	color: #fff;
	border: none;
	padding: 10px;
	border-radius: 4px;
	cursor: pointer;
	width: calc(100% - 20px);
}

input[type="submit"]:hover, button:hover {
	background-color: #45a049;
}

a {
	text-decoration: none;
	color: #3498db;
	font-weight: bold;
}

button[type="button"] {
	background-color: #3498db;
}
</style>
</head>
<body>
	<%
	Boolean cookieVerified = false;

	if (request.getCookies() != null) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("rememberme") && cookie.getValue() != null) {
		String cookieToken = cookie.getValue();
		String encriptedCookie = Aes.decrypt(cookieToken);
		String[] values = encriptedCookie.split("#");

		if (values.length == 3) {
			String usernameFromCookie = values[0];
			String passwordFromCookie = values[1];
			String tokenFromCookie = values[2];
			//verifica validità del cookie
			if (Token.verifyToken(usernameFromCookie, passwordFromCookie, tokenFromCookie)) {
				cookieVerified = true;
				request.setAttribute("usernameFromCookie", usernameFromCookie);
				request.setAttribute("passwordFromCookie", passwordFromCookie);
				//eliminazione della password
				passwordFromCookie = null;
			} else {
				System.out.println("\npresent cookie, but invalid");
			}
		} else {
			System.out.println("Errore nel cookie");
		}
		break;
			}
		}
	}
	System.out.println("\ncookie: " + cookieVerified);
	%>


	<form method="post" action="LoginServlet">
		<table>
			<tr>
				<td><input type="text" name="username" placeholder="Email"
					value="<%=(cookieVerified) ? request.getAttribute("usernameFromCookie") : ""%>"
					required></td>
			</tr>
			<tr>
				<td><input type="password" name="pass" placeholder="Password"
					value="<%=(cookieVerified) ? request.getAttribute("passwordFromCookie") : ""%>"
					required></td>
			</tr>
			<tr>
				<td>
					<!-- Aggiunta della casella "Remember Me" --> <input
					type="checkbox" name="rememberMe"> Remember me
				</td>
			</tr>
			<tr>
				<td><input type="submit" value="submit"></td>
			</tr>
			<tr>
				<td><a href="Registration.jsp">
						<button type="button">Register</button>
				</a></td>
			</tr>
		</table>
	</form>
</body>
</html>