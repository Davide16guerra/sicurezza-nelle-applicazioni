<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login Error</title>
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

div {
	background-color: #fff;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	padding: 20px;
	max-width: 400px;
	width: 100%;
	text-align: center;
}

p {
	margin: 0;
	font-size: 18px;
	color: #333;
}

a {
	text-decoration: none;
}

button {
	background-color: #e74c3c;
	color: #fff;
	border: none;
	padding: 10px;
	border-radius: 4px;
	cursor: pointer;
	margin-top: 10px;
}

button:hover {
	background-color: #c0392b;
}
</style>
</head>
<body>
	<div>
		<p>Login error</p>
		<a href="Login.jsp">
			<button type="button">OK</button>
		</a>
	</div>
</body>
</html>