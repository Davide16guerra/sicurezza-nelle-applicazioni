<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Registration</title>
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

input[type="text"], input[type="password"], input[type="file"] {
	width: 100%;
	padding: 10px;
	margin-bottom: 10px;
	box-sizing: border-box;
	border: 1px solid #ddd;
	border-radius: 4px;
}

input[type="text"]::placeholder, input[type="password"]::placeholder,
	input[type="file"]::placeholder {
	color: #777;
}

input[type="submit"], button {
	background-color: #4caf50;
	color: #fff;
	border: none;
	padding: 10px;
	border-radius: 4px;
	cursor: pointer;
	width: 100%;
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
	<form method="post" action="RegistrationServlet"
		enctype="multipart/form-data">
		<table>
			<tr>
				<td><input type="text" name="username" placeholder="Email"
					required></td>
			</tr>
			<tr>
				<td><input type="password" name="pass" placeholder="Password"
					required></td>
			</tr>
			<tr>
				<td><input type="password" name="confirmPass"
					placeholder="Confirm Password" required></td>
			</tr>
			<tr>
				<td><input type="file" name="file_image"
					placeholder="Image Profile" required></td>
			</tr>
			<tr>
				<td><input type="submit" value="Registration"></td>
			</tr>
			<tr>
				<td><a href="Login.jsp">
						<button type="button">Login</button>
				</a></td>
			</tr>
		</table>
	</form>
</body>
</html>