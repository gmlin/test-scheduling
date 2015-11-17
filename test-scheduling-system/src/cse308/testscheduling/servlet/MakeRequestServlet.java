package cse308.testscheduling.servlet;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cse308.testscheduling.Instructor;
import cse308.testscheduling.User;

/**
 * Servlet implementation class ScheduleExamServlet
 */
@WebServlet("/schedule_exam")
public class MakeRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// private static final Logger logger =
	// Logger.getLogger(ScheduleExamServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MakeRequestServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession s = request.getSession();
		Instructor instructor = ((User) (s.getAttribute("user"))).getInstructor();
		int duration = Integer.parseInt(request.getParameter("examDuration"));
		Timestamp startDateTime = Timestamp.valueOf(request.getParameter("startDateTime"));
		Timestamp endDateTime = Timestamp.valueOf(request.getParameter("endDateTime"));
		try {
			boolean success = false;
			if (request.getParameter("exam_type").equals("course")) {
				String courseId = request.getParameter("courseId");
				success = instructor.requestCourseExam(courseId, duration, startDateTime, endDateTime);
			} else if (request.getParameter("exam_type").equals("adhoc")) {
				String[] netIds = request.getParameter("netids").split("\n");
				success = instructor.requestAdHocExam(netIds, duration, startDateTime, endDateTime);
			}
			if (success) {
				request.getSession().setAttribute("message", "Successfully scheduled exam.");
				request.getSession().setAttribute("user", instructor.getUser());
			}
			else {
				request.getSession().setAttribute("message", "Cannot schedule exam.");
			}
		} catch (Exception e) {
			request.getSession().setAttribute("message", e.toString());
			// logger.log(Level.SEVERE, "Error in making Exam", e);
		} finally {
			response.sendRedirect(request.getHeader("Referer"));
			// logger.exiting(getClass().getName(), "doPost");
		}
	}
}
