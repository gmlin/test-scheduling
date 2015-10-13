<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% 
	if (session.getAttribute("account") == null)
		response.sendRedirect("Login.jsp");
%>

<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Testing Center Scheduling System</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link href="css/styles.css" rel="stylesheet">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/scripts.js"></script>
</head>
<body>

	<div class="container-fluid">
		<%@ include file="Header.jsp"%>
		<div class="row">
			<div class="col-sm-4">
				<%@ include file="AdminSidebar.jsp"%>
				<%@ include file="InstructorSidebar.jsp"%>
				<%@ include file="StudentSidebar.jsp"%>
			</div>
			<div class="col-sm-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4>Notices</h4></div>
                    <div class="panel-body">
                        <p>Blablabla.</p>
                    </div>
                </div>
			</div>
		</div>
	</div>
</body>
</html>