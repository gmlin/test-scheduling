<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	if (session.getAttribute("user") != null) {
		response.sendRedirect("Index.jsp");
	}
%>

<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.min.css">
<script src="/test-scheduling-system/JavaScriptServlet"></script>
</head>
<body>
	<div class="container-fluid">
		<form method="post" action="login">
			<%
				if ((Boolean) (session.getAttribute("incorrect")) != null) {
					out.println("Incorrect username/password.");
					session.removeAttribute("incorrect");
				}
				if ((Boolean) (session.getAttribute("logout")) != null) {
					out.println("Successfully logged out.");
					session.removeAttribute("logout");
				}
			%>
			<h2 class="text-center">Please sign in</h2>
			<input type="text" class="form-control" placeholder="Username"
				name="username" required> <input type="password"
				class="form-control" placeholder="Password" name="password" required>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Log
				In</button>
		</form>
	</div>
</body>
</html>