<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="cse308.testscheduling.User"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<%
    User user = (User) (session.getAttribute("user"));
    if (user == null || user.getStudent() == null)
        response.sendRedirect("Index.jsp");
%>

<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View appointments</title>
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
						<h4 class="text-center">Appointments</h4>
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
									<th>Term</th>
									<th>Appointment ID</th>
									<th>Exam ID</th>
									<th>Date</th>
									<th>Time</th>
									<th>Duration</th>
									<th>Seat Number</th>
									<th>Attended</th>
									<th>Cancel</th>
								</thead>
								<tbody>
									<c:forEach var="appt"
										items="${sessionScope.user.student.sortedAppointments}">
										<tr>
											<td>${appt.exam.course.term.season} ${appt.exam.course.term.year}</td>
											<td>${appt.id}</td>
											<td>${appt.exam.examId }
											<td>${appt.dateString }</td>
											<td>${appt.timeString }</td>
											<td>${appt.exam.duration }</td>
											<td>${appt.seat.seatNumber }</td>
											<td><c:if test="${appt.attendance }">
											 Yes
											 </c:if> <c:if test="${not appt.attendance }">
											 No
                                             </c:if></td>
											<c:if test="${appt.cancelable }">
												<td><a href="cancel_appt?cancel=${appt.id }">Cancel</a></td>
											</c:if>
											<c:if test="${not appt.cancelable }">
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