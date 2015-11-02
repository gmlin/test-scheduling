package cse308.testscheduling;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ScheduleExamServlet
 */
@WebServlet("/schedule_exam")
public class ScheduleExamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private static final Logger logger = Logger.getLogger(ScheduleExamServlet.class.getName());
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ScheduleExamServlet() {
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
		Instructor instructor = ((User)(s.getAttribute("user"))).getInstructor();
		int duration = Integer.parseInt(request.getParameter("examDuration"));
		Timestamp startDateTime = Timestamp.valueOf(request.getParameter("startDateTime").replace("T", " ") + ":00");
		Timestamp endDateTime = Timestamp.valueOf(request.getParameter("endDateTime").replace("T", " ") + ":00");
		try {
			if (request.getParameter("exam_type").equals("course")) {
				String courseId = request.getParameter("courseId");
				instructor.requestCourseExam(courseId, duration, startDateTime, endDateTime);
			}
			else if (request.getParameter("exam_type").equals("adhoc")) {
				String[] netIds = request.getParameter("netids").split("\n");
				instructor.requestAdHocExam(netIds, duration, startDateTime, endDateTime);
			}
			request.getSession().setAttribute("message", "Successfully scheduled exam.");
		}
		catch(Exception e) {
			request.getSession().setAttribute("message", e.toString());
			//logger.log(Level.SEVERE, "Error in making Exam", e);
		}
		finally {
			response.sendRedirect(request.getHeader("Referer"));
			//logger.exiting(getClass().getName(), "doPost");
		}
	}
}
