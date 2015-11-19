<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="cse308.testscheduling.User"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<%
	User user = (User) (session.getAttribute("user"));
	if (user == null || user.getAdministrator() == null)
		response.sendRedirect("Index.jsp");
%>

<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Import Data</title>
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
				<c:if test="${not empty sessionScope.user.administrator}">
					<%@ include file="AdminSidebar.jsp"%>
				</c:if>
				<c:if test="${not empty sessionScope.user.instructor}">
					<%@ include file="InstructorSidebar.jsp"%>
				</c:if>
				<c:if test="${not empty sessionScope.user.student}">
					<%@ include file="StudentSidebar.jsp"%>
				</c:if>
			</div>
			<div class="col-sm-7">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="text-center">Import Data</h4>
					</div>
					<div class="panel-body">
						<form action="import_data" method="post" enctype = "multipart/form-data">
						<%
								if (session.getAttribute("message") != null) {
									out.println(session.getAttribute("message"));
									session.removeAttribute("message");
								}
							%>
							<br>
							<label>User</label><input type="file" name="file1" size="100" required/>
							<br>
							<label>Class</label><input type="file" name="file2" size="100" required/>
							<br>
							<label>Roster</label><input type="file" name="file3" size="100" required/>
							<br>
							<label>Instructor</label><input type="file" name="file4" size="100" required/>
							<br>
							<button type="submit" class="btn btn-default">Submit</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>