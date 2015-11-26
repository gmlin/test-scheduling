<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="cse308.testscheduling.User"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<%
	User user = (User) (session.getAttribute("user"));
	if (user == null || user.getAdministrator() == null || session.getAttribute("appt") == null)
		response.sendRedirect("Index.jsp");
%>

<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modify appointment</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link href="css/styles.css" rel="stylesheet">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/scripts.js"></script>
<script src="/test-scheduling-system/JavaScriptServlet"></script>
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
						<h4 class="text-center">Appointment</h4>
					</div>
					<div class="panel-body">
						<%
							if (session.getAttribute("message") != null) {
								out.println(session.getAttribute("message"));
								session.removeAttribute("message");
							}
						%>
						<form action="modify_appt" method="post">
							<input type="hidden" name="apptId" value="${appt.id }">
							<table class="table">
								<thead>
									<th>Appointment ID</th>
									<th>Exam ID</th>
									<th>Date/Time</th>
									<th>Duration</th>
									<th>Seat Number</th>
									<th>Attended</th>
									<th>Cancel</th>
								</thead>
								<tbody>
									<tr>
										<td>${appt.id}</td>
										<td>${appt.exam.examId }
										<td><input type="text" name="dateTime"
											value="${appt.dateTime }"></td>
										<td>${appt.exam.duration }</td>
										<td><input type="number" name="seat"
											value="${appt.seat.seatNumber }"></td>
										<td><c:if test="${appt.attendance }">
												<input type="radio" name="attended" value="yes" checked>Yes
											<input type="radio" name="attended" value="no">No
											</c:if> <c:if test="${not appt.attendance }">
												<input type="radio" name="attended" value="yes">Yes
                                            <input type="radio"
													name="attended" value="no" checked>No
											</c:if></td>
										<td><a href="modify_appt?cancel=${appt.id }">Cancel</a></td>
									</tr>
								</tbody>
							</table>
							<button type="submit" class="btn btn-default">Submit</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>