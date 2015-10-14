<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="cse308.testscheduling.User"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	User user = (User)(session.getAttribute("user"));
	if (user == null || user.getInstructor() == null)
		response.sendRedirect("Index.jsp");
%>

<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Schedule Course Exam</title>
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
						<h4 class="text-center">Schedule Course Exam</h4>
					</div>
					<div class="panel-body">
						<form>
							<div class="form-group">
								<label for="courseId">Course</label> <select
									class="form-control" id="courseId">
									<c:forEach items="${sessionScope.user.instructor.courses}" var="course">
									   <option>${course.courseId}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label for="examDuration">Duration</label> <input
									type="number" class="form-control" id="examDuration"
									placeholder="minutes">
							</div>
							<div class="form-group">
								<label for="startDateTime">Start</label> <input
									type="datetime-local" id="startDateTime">
							</div>
							<div class="form-group">
                                <label for="endDateTime">End</label><input
                                    type="datetime-local" id="endDateTime">
                            </div>
							<button type="submit" class="btn btn-default">Submit</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>