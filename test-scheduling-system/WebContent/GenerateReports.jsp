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
<title>Generate reports</title>
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
                        <h4 class="text-center">Generate report</h4>
                    </div>
                    <div class="panel-body">
                    
                    	<form action="generate_report" method="get">
							<div class="form-group">
								<label for="termID">Term</label><select
									class="form-control" name="termID" id="termID" required>
									<c:forEach items="${sessionScope.user.administrator.allTerms}"
										var="term">
										<option value="${term.termID}">${term}
												(${term.season} ${term.year})</option>
									</c:forEach>
								</select>
							</div>
							
							<button type="submit" class="btn btn-default" 
								name="report" value="daily">Daily Appointment Report</button>
							<button type="submit" class="btn btn-default" 
								name="report" value="weekly">Weekly Appointment Report</button>
							<button type="submit" class="btn btn-default" 
								name="report" value="term">Term Course Report</button>
							<br></br>
							<br></br>
							<div class="form-group">
								<label for="termStart">Term Start</label><select
									class="form-control" name="termStart" id="termStart" required>
									<c:forEach items="${sessionScope.user.administrator.allTerms}"
										var="term">
										<option value="${term.termID}">${term}
												(${term.season} ${term.year})</option>
									</c:forEach>
								</select>
							</div>	
							<div class="form-group">
								<label for="termEnd">Term End</label><select
									class="form-control" name="termEnd" id="termEnd" required>
									<c:forEach items="${sessionScope.user.administrator.allTerms}"
										var="term">
										<option value="${term.termID}">${term}
												(${term.season} ${term.year})</option>
									</c:forEach>
								</select>
							</div>
							<button type="submit" class="btn btn-default" 
								name="report" value="termrange">Term Range Appointment Report</button>
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