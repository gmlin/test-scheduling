<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% 
	if (session.getAttribute("account") != null) {
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
</head>
<body>
	<div class="container">
		<form method="post" action="LoginServlet">
			<h2 class="text-center">Please sign in</h2>
			<input type="text" class="form-control" placeholder="Username" name="username" required>  
			<input type="password" class="form-control" placeholder="Password" name="password" required>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Log In</button>
		</form>
		<% 
			if ((Boolean)(session.getAttribute("incorrect")) != null) {
				out.println("Incorrect username/password.");
				session.removeAttribute("incorrect");
			}
			if ((Boolean)(session.getAttribute("logout")) != null) {
				out.println("Successfully logged out.");
				session.removeAttribute("logout");
			}
		%>
	</div>
</body>
</html>