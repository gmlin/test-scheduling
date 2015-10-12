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
		<form class="form-signin" method="post" action="LoginServlet">
			<h2 class="form-signin-heading text-center">Please sign in</h2>
			<label for="inputUsername" class="sr-only">Username</label> <input
				type="text" id="inputUsername" class="form-control"
				placeholder="Username" name="username" required autofocus> <label
				for="inputPassword" class="sr-only">Password</label> <input
				type="password" id="inputPassword" class="form-control"
				placeholder="Password" name="password" required>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Sign
				in</button>
		</form>
		<% 
			if ((Boolean)(session.getAttribute("incorrect")) != null) {
				out.println("Incorrect username/password.");
				session.removeAttribute("incorrect");
			}
		%>
	</div>
</body>
</html>