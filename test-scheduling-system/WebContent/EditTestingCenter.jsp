<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="cse308.testscheduling.User"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
    User user = (User)(session.getAttribute("user"));
    if (user == null || user.getAdministrator() == null)
        response.sendRedirect("Index.jsp");
%>

<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Testing Center Information</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link href="css/styles.css" rel="stylesheet">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/scripts.js"></script>
<script type="text/javascript" src="//cdn.jsdelivr.net/jquery/1/jquery.min.js"></script>
<script type="text/javascript" src="//cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
<link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap/latest/css/bootstrap.css" />
 
<!-- Include Date Range Picker -->
<script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
<link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css" />
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
						<form>
							<div class="form-group">
								<label for="numSeats">Number of Seats</label> <input
									type="number" class="form-control" id="numSeats">
							</div>
							<div class="form-group">
								<label for="numSetAside">Number of Set-Aside Seats</label> <input
									type="number" class="form-control" id="numSetAside">
							</div>
							<div class="form-group">
								<label>Testing Center Hours</label><br>
								<label for="openTime">Open Time</label> <input
									type="time" id="openTime">
							</div>
							<div class="form-group">
								<label for="closeTime">Close Time</label><input type="time"
									id="closeTime">
							</div>
			<!-- TODO:We should be able to add multiple ranges -->
							<div class="form-group">
								<label for="closedDateRanges">Closing Dates</label> <input type="text" name="closedDateRanges" id="closedDateRanges">								
								<script type="text/javascript">
								$(function() {
    								$('input[name="closedDateRanges"]').daterangepicker();
								});
								</script>
							</div>
							
			<!-- TODO:We should be able to add multiple periods -->
							<div class="form-group">
								<label for="reservedPeriods">Reserved Periods</label> <input type="text" name="reservedPeriods" id="reservedPeriods">								
								<script type="text/javascript">
								$(function() {
								    $('input[name="reservedPeriods"]').daterangepicker({
								        timePicker: true,
								        timePickerIncrement: 30,
								        locale: {
								            format: 'MM/DD/YYYY h:mm A'
								        }
								    });
								});
								</script>
							</div>
							<div class="form-group">
								<label for="gapTime">Appointment Gap Time</label> <input
									type="number" class="form-control" id="gapTime" max=30 min=0>
							</div>
							<div class="form-group">
								<label for="reminderInterval">Reminder Interval</label> <input
									type="number" class="form-control" id="reminderInterval">
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