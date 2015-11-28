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
						<form action="ViewAppointments.jsp" method="get">
							<div class="form-group">
								<label for="termID">Term</label> <select class="form-control"
									name="termID" id="termID" required>
									<c:if test="${not empty param.termID }">
										<c:forEach items="${sessionScope.user.allTerms}" var="term">
											<c:if test="${term.termID == param.termID}">
												<option value="${term.termID}" selected>${term}
													(${term.season} ${term.year})</option>
											</c:if>
											<c:if test="${term.termID != param.termID}">
												<option value="${term.termID}">${term}
													(${term.season} ${term.year})</option>
											</c:if>
										</c:forEach>
									</c:if>
									<c:if test="${empty param.termID }">
										<c:forEach items="${sessionScope.user.allTerms}" var="term">
											<c:if test="${term.current }">
												<option value="${term.termID}" selected>${term}
													(${term.season} ${term.year})</option>
											</c:if>
											<c:if test="${ not term.current }">
												<option value="${term.termID}">${term}
													(${term.season} ${term.year})</option>
											</c:if>
										</c:forEach>
									</c:if>

								</select>
							</div>
							<button type="submit" class="btn btn-default">Set</button>

						</form>
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
							<c:if test="${not empty param.termID }">
                                        <c:forEach var="appt"
                                    items="${sessionScope.user.student.getTermAppointments(param.termID)}">
                                    <tr>
                                        <td>${appt.exam.term.season}${appt.exam.term.year}</td>
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
                                    </c:if>
                                    <c:if test="${empty param.termID }">
                                        <c:forEach var="appt"
                                    items="${sessionScope.user.student.getTermAppointments(sessionScope.user.currentTerm.termID)}">
                                    <tr>
                                        <td>${appt.exam.term.season}${appt.exam.term.year}</td>
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
                                    </c:if>
								
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>