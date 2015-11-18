<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="cse308.testscheduling.User"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<%
	User user = (User) (session.getAttribute("user"));
	if (user == null || user.getInstructor() == null)
		response.sendRedirect("Index.jsp");
%>

<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Exam Requests</title>
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
						<h4 class="text-center">Exam Requests</h4>
					</div>
					<div class="panel-body">
						<%
							if (session.getAttribute("message") != null) {
								out.println(session.getAttribute("message"));
								session.removeAttribute("message");
							}
						%>
						<table class="table">
							<thead>
								<th>Exam ID</th>
								<th>Start Time</th>
								<th>End Time</th>
								<th>Duration</th>
								<th>Number of examinees</th>
								<th>Appointments attended</th>
								<th>Status</th>
								<th>Cancel</th>
							</thead>
							<tbody>
								<c:forEach var="exam"
									items="${sessionScope.user.instructor.allExams}">
									<tr>
										<td>${exam.examId}</td>
										<td>${exam.startDateTime }</td>
										<td>${exam.endDateTime }</td>
										<td>${exam.duration }</td>
										<c:if test="${exam.adHoc}">
										    <td>${fn:length(exam.students)}</td>
										</c:if>
										<c:if test="${not exam.adHoc}">
											<td>${fn:length(exam.course.students)}</td>
										</c:if>
										<td>${exam.attendance}</td>
										<td>${exam.status }</td>
										<c:if test="${exam.status == 'PENDING' or exam.status == 'DENIED'}">
											<td><a href="cancel_request?cancel=${exam.examId }">Cancel</a></td>
										</c:if>
										<c:if test="${exam.status != 'PENDING' and exam.status != 'DENIED'}">
											<td>N/A</td>
										</c:if>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>