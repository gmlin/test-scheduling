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
<title>Add Closed Dates and Reserved Periods</title>
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
                        <h4 class="text-center">Utilization</h4>
                    </div>
                    <div class="panel-body">
                    	<form action="utilization" method="get">
							<div class="form-group">
								<label for="utilizationDateRange">Date Range</label> <input
									type="text" name="utilizationDateRange" id="utilizationDateRange">
								<script type="text/javascript">
								$(function() {
    								$('input[name="utilizationDateRange"]').daterangepicker();
								});
								</script>
							</div>
							<button type="submit" class="btn btn-default">Submit</button>
							<br></br>
							<br></br>
							<%
								if (session.getAttribute("message") != null) {
									out.println(session.getAttribute("message"));
									session.removeAttribute("message");
								}
							%>
						</form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>