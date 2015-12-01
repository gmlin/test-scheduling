<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page
    import="cse308.testscheduling.User"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<%
    User user = (User) (session.getAttribute("user"));
    if (user == null || user.getInstructor() == null)
        response.sendRedirect("Index.jsp");
    if (session.getAttribute("requestexam") != null)
    	session.removeAttribute("requestexam");
    else {
    	response.sendRedirect("Index.jsp");
    }
%>

<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Exam Utilization</title>
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
                        <h4 class="text-center">Exam Utilization</h4>
                    </div>
                    <div class="panel-body">
                            <%
                            if (session.getAttribute("message") != null) {
                                out.println(session.getAttribute("message"));
                                session.removeAttribute("message");
                            }
                        	%>
                        	<%
                            if (session.getAttribute("instructorreport") != null) {
                                out.println(session.getAttribute("instructorreport"));
                                session.removeAttribute("instructorreport");
                            }
                        	%>
                        	<br></br>
                        <form action="schedule_exam" method="post">
                            <button type="submit" class="btn btn-default">Confirm</button>
                            <a href="Index.jsp">Cancel</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>