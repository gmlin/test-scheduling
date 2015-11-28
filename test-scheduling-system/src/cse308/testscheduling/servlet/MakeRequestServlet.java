package cse308.testscheduling.servlet;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
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
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userid");
		EntityManager em = DatabaseManager.createEntityManager();
		int duration = Integer.parseInt(request.getParameter("examDuration"));
		Timestamp startDateTime = Timestamp.valueOf(request.getParameter("startDateTime"));
		Timestamp endDateTime = Timestamp.valueOf(request.getParameter("endDateTime"));
		try {
			User user = em.find(User.class, userId);
			Instructor instructor = user.getInstructor();
			boolean success = false;
			if (request.getParameter("exam_type").equals("course")) {
				String courseId = request.getParameter("courseId");
				success = instructor.requestCourseExam(em, courseId, duration, startDateTime, endDateTime);
			} else if (request.getParameter("exam_type").equals("adhoc")) {
				String[] netIds = request.getParameter("netids").split("\n");
				success = instructor.requestAdHocExam(em, netIds, duration, startDateTime, endDateTime);
			}
			if (success) {
				session.setAttribute("message", "Successfully scheduled exam.");
				session.setAttribute("user", user);
			}
			else {
				session.setAttribute("message", "Cannot schedule exam.");
			}
		} catch (Exception e) {
			session.setAttribute("message", e.getMessage());
			// logger.log(Level.SEVERE, "Error in making Exam", e);
		} finally {
			em.close();
			response.sendRedirect(request.getHeader("Referer"));
			// logger.exiting(getClass().getName(), "doPost");
		}
	}
}
