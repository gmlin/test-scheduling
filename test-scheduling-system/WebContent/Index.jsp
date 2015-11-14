<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="java.util.List,cse308.testscheduling.User"%>

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
						<c:if test="${not empty sessionScope.user.administrator}">
							<%
								User u = (User) session.getAttribute("user");
									session.setAttribute("numPending", u.getAdministrator().getPendingExams().size());
							%>
							<p>
								Number of exam requests awaiting approval:
								<c:out value="${sessionScope.numPending}" />
							</p>
						</c:if>
						<c:if test="${not empty sessionScope.user.instructor}">

							<c:forEach var="course"
								items="${sessionScope.user.instructor.courses}">
								<c:forEach var="exam" items="${course.exams}">
									<c:if
										test="${exam.status == 'APPROVED' or exam.status == 'DENIED'}">
										<p>
											Exam
											<c:out value="${exam.examId}" />
											has been
											<c:out value="${exam.status}" />
											.
										</p>
									</c:if>
								</c:forEach>
							</c:forEach>
							<c:forEach var="adHocExam"
								items="${sessionScope.user.instructor.adHocExams}">
								<c:if
									test="${adHocExam.status == 'APPROVED' or adHocExam.status == 'DENIED'}">
									<p>
										Exam
										<c:out value="${adHocExam.examId}" />
										has been
										<c:out value="${adHocExam.status}" />
										.
									</p>
								</c:if>
							</c:forEach>
						</c:if>
						<c:if test="${not empty sessionScope.user.student}">
							<c:forEach var="course"
								items="${sessionScope.user.student.courses}">
								<c:forEach var="exam" items="${course.exams}">
									<c:if test="${exam.status == 'APPROVED'}">
										<p>
											<c:out value="${exam.examId}" />
										</p>
									</c:if>
								</c:forEach>
							</c:forEach>
							<c:forEach var="adHocExam"
								items="${sessionScope.user.student.adHocExams}">
								<c:if test="${adHocExam.status == 'APPROVED'}">
									<p>
										<c:out value="${adHocExam.examId}" />
									</p>
								</c:if>
							</c:forEach>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>