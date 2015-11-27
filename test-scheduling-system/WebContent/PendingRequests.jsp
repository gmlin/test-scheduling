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
<title>View Exam Requests</title>
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
						<h4 class="text-center">Approve/Deny Exam Requests</h4>
					</div>
					<div class="panel-body">
						<form action="modify_request" method="post">
							<%
								if (session.getAttribute("message") != null) {
									out.println(session.getAttribute("message"));
									session.removeAttribute("message");
								}
							%>
							<div class="form-group">
								<table class="table">
									<thead>
										<th>Term</th>
										<th>Exam ID</th>
										<th>Course/Instructor</th>
										<th>Start Time</th>
										<th>End Time</th>
										<th>Duration</th>
										<th>Number of examinees</th>
										<th>Approve</th>
										<th>Deny</th>
									</thead>
									<tbody>
										<c:forEach
											items="${sessionScope.user.administrator.pendingExams}"
											var="exam">
											<tr>
												<td>${exam.course.term.season} ${exam.course.term.year}</td>
												<td>${exam.examId }</td>
												<td><c:if test="${exam.adHoc}">
                                                        ${exam.instructor}
                                                    </c:if> <c:if
														test="${not exam.adHoc}">
                                                        ${exam.course}
                                                    </c:if></td>
												<td>${exam.startDateTime}</td>
												<td>${exam.endDateTime }</td>
												<td>${exam.duration }</td>
												<td><c:if test="${exam.adHoc}">
                                                        ${fn:length(exam.students)}
                                                    </c:if> <c:if
														test="${not exam.adHoc}">
                                                        ${fn:length(exam.course.students)}
                                                    </c:if></td>
												<td><input type="radio" name="${exam.examId }"
													value="approve" /></td>
												<td><input type="radio" name="${exam.examId }"
													value="deny" /></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
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