<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="cse308.testscheduling.User"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	User user = (User) (session.getAttribute("user"));
	if (user == null || user.getAdministrator() == null)
		response.sendRedirect("Index.jsp");
%>

<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Testing Center Information</title>
<link rel="stylesheet" type="text/css"
	href="//cdn.jsdelivr.net/bootstrap/latest/css/bootstrap.css" />
<link rel="stylesheet" href="css/bootstrap.min.css">
<link href="css/styles.css" rel="stylesheet">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/scripts.js"></script>
<script type="text/javascript"
	src="//cdn.jsdelivr.net/jquery/1/jquery.min.js"></script>
<script type="text/javascript"
	src="//cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>

<!-- Include Date Range Picker -->
<script type="text/javascript"
	src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
<link rel="stylesheet" type="text/css"
	href="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css" />
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
						<h4 class="text-center">Edit Testing Center Information</h4>
					</div>
					<div class="panel-body">
						<form action="set_term" method="post">
							<%
								if (session.getAttribute("message") != null) {
									out.println(session.getAttribute("message"));
									session.removeAttribute("message");
								}
							%>
							<div class="form-group">
								<label for="termID">Current Term</label> <select
									class="form-control" name="termID" id="termID" required>
									<c:forEach items="${sessionScope.user.allTerms}"
										var="term">
										<c:if test="${term.current }">
											<option value="${term.termID}" selected>${term}
												(${term.season} ${term.year})</option>
										</c:if>
										<c:if test="${ not term.current }">
											<option value="${term.termID}">${term}
												(${term.season} ${term.year})</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
							<button type="submit" class="btn btn-default">Set</button>

						</form>
						<form action="edit_testing_center_info" method="post">
							<%
								if (session.getAttribute("message") != null) {
									out.println(session.getAttribute("message"));
									session.removeAttribute("message");
								}
							%>
							<c:set var="testingCenter"
								value="${sessionScope.user.currentTerm.testingCenter}"
								scope="session" />

							<input type="hidden" name="changeterm" value="no" />
							<div class="form-group">
								<label for="numSeats">Number of Seats</label> <input
									type="number" class="form-control"
									value=${sessionScope.testingCenter.numSeats } id="numSeats"
									name="numSeats">
							</div>
							<div class="form-group">
								<label for="numSetAside">Number of Set-Aside Seats</label> <input
									type="number" class="form-control"
									value=${sessionScope.testingCenter.numSetAsideSeats
									}
									id="numSetAside" name="numSetAside">
							</div>
							<div class="form-group">
								<label>Testing Center Hours</label><br> <label
									for="openTime">Open Time</label> <input type="time"
									value=${sessionScope.testingCenter.openTimeString
									}
									id="openTime" name="openTime">
							</div>
							<div class="form-group">
								<label for="closeTime">Close Time</label><input type="time"
									value=${sessionScope.testingCenter.closeTimeString
									}
									id="closeTime" name="closeTime">
							</div>
							<div class="form-group">
								<label for="gapTime">Appointment Gap Time</label> <input
									type="number" class="form-control" id="gapTime" max=30 min=0
									value=${sessionScope.testingCenter.gapTime
									}
									name="gapTime">
							</div>
							<div class="form-group">
								<label for="reminderInterval">Reminder Interval</label> <input
									type="number" class="form-control" id="reminderInterval"
									value=${sessionScope.testingCenter.reminderInterval
									}
									name="reminderInterval">
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