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
<title>Schedule Ad Hoc Exam</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link href="css/styles.css" rel="stylesheet">
<link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/scripts.js"></script>
<script src="js/bootstrap-datetimepicker.min.js"></script>
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
						<h4 class="text-center">Schedule Ad Hoc Exam</h4>
					</div>
					<div class="panel-body">
						<form action="request_utilization" method="post">
							<input type="hidden" name="exam_type" value="adhoc" />
							<%
                                if (session.getAttribute("message") != null) {
                                    out.println(session.getAttribute("message"));
                                    session.removeAttribute("message");
                                }
                            %>
							<div class="form-group">
								<label for="examDuration">Duration</label> <input type="number"
									class="form-control" name="examDuration" id="examDuration"
									placeholder="minutes" required>
							</div>
							<div class="form-group">
								<label for="startDateTime">Start</label> <input value=""
									type="text" name="startDateTime" id="startDateTime"
									class="dateTime" readonly required>
							</div>
							<div class="form-group">
								<label for="endDateTime">End</label> <input value="" type="text"
									name="endDateTime" id="endDateTime" class="dateTime" readonly
									required>
							</div>
							<div class="form-group">
								<label for="netids">Examinee NetIDs</label>
								<textarea name="netids" id="netids" class="form-control"
									rows="10" placeholder="One per line" required></textarea>
							</div>
							<button type="submit" class="btn btn-default">Submit</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
        $(".dateTime").datetimepicker({
            format : 'yyyy-mm-dd hh:ii:00',
            startDate : new Date(),
            minuteStep : 30,
            autoclose: true
        });
    </script>
</body>
</html>