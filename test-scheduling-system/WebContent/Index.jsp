<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

<%
	if (session.getAttribute("user") == null)
		response.sendRedirect("Login.jsp");
%>

<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Testing Center Scheduling System</title>
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
						<h4>Notices</h4>
					</div>
					<div class="panel-body">
						<sql:setDataSource var="db" driver="com.mysql.jdbc.Driver"
							url="jdbc:mysql://mysql2.cs.stonybrook.edu:3306/cse308mingteam"
							user="cse308mingteam" password="changeit" />
						<c:if test="${not empty sessionScope.user.administrator}">
							<sql:query dataSource="${db}" var="result">
							SELECT COUNT(*) AS count FROM exam WHERE status = 0;
							</sql:query>
							<p>Number of scheduled exams awaiting approval: 
							<c:out value="${result.rows[0].count}"/>
							</p>
						</c:if>
						<c:if test="${not empty sessionScope.user.instructor}">
						  <c:forEach var="course" items="${sessionScope.user.instructor.courses}">
							<sql:query dataSource="${db}" var="result">
                            SELECT * FROM exam WHERE COURSE_ID = "${course.courseId}" AND STATUS != "COMPLETED";
                            </sql:query>
                            <c:forEach var="row" items="${result.rows}">
	                            <p>
	                            <c:out value="${row.EXAM_ID}"/>: 
	                            <c:out value="${row.status}"/>
	                            </p>
	                        </c:forEach>
                          </c:forEach>
                          <c:forEach var="adHocExam" items="${sessionScope.user.instructor.adHocExams}">
                            <c:if test="${adHocExam.status != 'COMPLETED'}">
                                <p>
                                <c:out value="${adHocExam.examId}"/>: 
                                <c:out value="${adHocExam.status}"/>
                                </p>
                            </c:if>
                          </c:forEach>
						</c:if>
						<c:if test="${not empty sessionScope.user.student}">
							<%@ include file="StudentSidebar.jsp"%>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>