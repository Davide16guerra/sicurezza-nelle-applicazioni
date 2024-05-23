<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Home.HomeDao"%>
<%@ page import="java.util.Base64"%>
<%@ page import="Utility.Proposal"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Home</title>
<style>
body {
	font-family: 'Arial', sans-serif;
	background-color: #f7f7f7;
	margin: 0;
	padding: 20px;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	height: 100vh;
}

h1 {
	display: flex;
	align-items: center;
	margin-bottom: 20px;
}

img {
	max-width: 100px;
	border-radius: 50%;
	margin-left: 10px;
}

input[type="file"] {
	margin-bottom: 20px;
}

table {
	width: 100%;
	margin-top: 20px;
	border-collapse: collapse;
}

th, td {
	border: 1px solid #ddd;
	padding: 15px;
	text-align: left;
}

a {
	display: block;
	text-decoration: none;
	color: #333;
	padding: 10px;
	margin-bottom: 5px;
}

a:hover {
	background-color: #ddd;
}

#textViewer {
	margin-top: 20px;
	text-align: left;
	background-color: #ddd;
	padding: 20px;
	border-radius: 10px;
	overflow: auto;
	max-width: 600px;
	margin: 0 auto;
	min-height: 200px;
	min-width: 300px;
}

h1, #proposalTitle {
	margin-bottom: 10px;
}

pre {
	white-space: pre-wrap;
}

.banner {
	display: none;
	padding: 10px;
	background-color: #f44336;
	color: white;
	text-align: center;
}
</style>

<script>
		<!--check tipo del file-->
        var previousValue = '';

        function handleFileSubmissionResult(success) {
            var feedbackMessage = document.getElementById('feedbackMessage');
            feedbackMessage.innerHTML = success ? 'File accepted successfully!' : 'File not accepted. Please check the file type and content.';
            feedbackMessage.style.color = success ? 'green' : 'red';

            // Hide the banner if the file is accepted
            if (success) {
                hideBanner();
            }
        }

        <!--feedback upload del file -->
        function handleInputChange() {
            var myInput = document.getElementById('myInput');
            var currentValue = myInput.value;
            var fileExtension = currentValue.split('.').pop().toLowerCase();

            if (currentValue !== previousValue) {
                previousValue = currentValue;
                if (fileExtension != 'txt') {
                    showBanner();
                }
            }
        }

        function showBanner() {
            var banner = document.getElementById('fileTypeErrorBanner');
            banner.style.display = 'block';
        }

        function hideBanner() {
            var banner = document.getElementById('fileTypeErrorBanner');
            banner.style.display = 'none';
        }

        <!--visualizzazione delle proposte progettuali -->
        function showProposal(title, contentBase64) {
            var titleElement = document.getElementById('proposalTitle');
            var contentElement = document.getElementById('proposalContent');

            titleElement.textContent = title;

            var decodedContent = atob(contentBase64);
            var range = document.createRange();
            range.setStart(contentElement, 0);
            var fragment = range.createContextualFragment(decodedContent);
            contentElement.innerHTML = '';
            contentElement.appendChild(fragment);
        }
    </script>
</head>
<body>
	<%
	String username = (String) request.getAttribute("username");
	HomeDao homeDao = new HomeDao(username);
	byte[] fileImageBytes = homeDao.getProfileImage();
	String fileImageBase64 = Base64.getEncoder().encodeToString(fileImageBytes);
	%>

	<h1>
		Welcome
		<%=username%>! <img src="data:image/jpeg;base64, <%=fileImageBase64%>"
			alt="Profile Image">
	</h1>

	<!-- Feedback message -->
	<div id="feedbackMessage"></div>

	<form method="post" action="HomeServlet" enctype="multipart/form-data">
		<input type="file" name="file" size="50" id="myInput"
			onchange="handleInputChange()" required> <input type="submit"
			value="Upload Proposal">
	</form>

	<div id="fileTypeErrorBanner" class="banner">Invalid file type.
		Please select a .txt file.</div>

	<!-- Feedback da HomeServlet -->
	<%
	Boolean fileAccepted = (Boolean) request.getAttribute("fileAccepted");
	if (fileAccepted != null) {
	%>
	<script>
                handleFileSubmissionResult(<%=fileAccepted%>);
            </script>
	<%
	}
	%>

	<table>
		<thead>
			<tr>
				<th>Username</th>
				<th>Proposal</th>
			</tr>
		</thead>
		<tbody>
			<%
			List<Proposal> proposalList = homeDao.getProposalsList();
			for (Proposal proposal : proposalList) {
				String linkText = String.format("[%s] %s", proposal.getUsername(), proposal.getProposalName());
				String contentBase64 = Base64.getEncoder().encodeToString(proposal.getProposal());
			%>
			<tr>
				<td><%=proposal.getUsername()%></td>
				<td><a href="#"
					onclick="showProposal('<%=linkText%>', '<%=contentBase64%>')"><%=proposal.getProposalName()%></a></td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>

	<!-- Display testo selezionato -->
	<p></p>
	<div id="textViewer">
		<h1 id="proposalTitle"></h1>
		<pre id="proposalContent"></pre>
	</div>

	<form action="LogoutServlet" method="get">
		<p></p>
		<p></p>
		<input type="submit" value="Logout">
	</form>
</body>
</html>