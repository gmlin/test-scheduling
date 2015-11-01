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
	private static final Logger logger = Logger.getLogger(ScheduleExamServlet.class.getName());
	

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
		if (request.getParameter("exam_type").equals("course")) {
			String courseId = request.getParameter("courseId");
			instructor.requestCourseExam(courseId, duration, startDateTime, endDateTime);
		}
		else if (request.getParameter("exam_type").equals("adhoc")) {
			String[] netIds = request.getParameter("netids").split("\n");
			instructor.requestAdHocExam(netIds, duration, startDateTime, endDateTime);
		}
		/*
		logger.entering(getClass().getName(), "doPost", request);
		File f = new File("/ExamRequestTest.txt");
		FileHandler fh = new FileHandler("ExamRequestTest.txt");
		logger.addHandler(fh);
		try {
			exam.setDuration(Integer.parseInt(request.getParameter("examDuration")));
			exam.setStartDateTime(Timestamp.valueOf(request.getParameter("startDateTime").replace("T", " ") + ":00"));
			exam.setEndDateTime(Timestamp.valueOf(request.getParameter("endDateTime").replace("T", " ") + ":00"));
			if (request.getParameter("exam_type").equals("course")) {
				exam.setAdHoc(false);
				Course course = em.find(Course.class, request.getParameter("courseId"));
				exam.setCourse(course);
				course.addExam(exam);
				exam.setExamId(course.getCourseId() + "_ex" + String.valueOf(course.getExams().size()));
				logger.log(Level.SEVERE, "Regular Exam Sucessfully Requested for " + course.getCourseId() + 
						" . Duration is: " + request.getParameter("examDuration") +
						". StartDate is " + request.getParameter("startDateTime").replace("T", " ") + ":00" +
						". EndDate is " + request.getParameter("endDateTime").replace("T", " ") + ":00");
			}
			else if (request.getParameter("exam_type").equals("adhoc")) {
				exam.setAdHoc(true);
				Query query = em.createQuery("SELECT e FROM Exam e WHERE e.adHoc = true", Exam.class);
				exam.setExamId("adhoc_ex" + (query.getResultList().size() + 1));
				String[] netIds = request.getParameter("netids").split("\n");
				User u;
				for (String netId : netIds) {
					query = em.createQuery("SELECT u FROM User u WHERE u.netId = :username", User.class);
					query.setParameter("username", netId.trim());
					u = (User)(query.getSingleResult());
					u.getStudent().addAdHocExam(exam);
					exam.addStudent(u.getStudent());
				}
				HttpSession s = request.getSession();
				Instructor instructor = ((User)(s.getAttribute("user"))).getInstructor();
				exam.setInstructor(instructor);
				instructor.addAdHocExam(exam);
				logger.log(Level.SEVERE, "AdHoc Exam Sucessfully Requested" + 
						" . Duration is: " + request.getParameter("examDuration") +
						". StartDate is " + request.getParameter("startDateTime").replace("T", " ") + ":00" +
						". EndDate is " + request.getParameter("endDateTime").replace("T", " ") + ":00");
			}
			em.persist(exam);
			em.getTransaction().commit();
			request.getSession().setAttribute("message", "Successfully scheduled exam.");
			
		}
		catch (Exception e) {
			request.getSession().setAttribute("message", e.toString());
			logger.log(Level.SEVERE, "Error in making Exam", e);
			
		}
		finally {
			logger.exiting(getClass().getName(), "doPost");
			
			em.close();
			emf.close();
			response.sendRedirect(request.getHeader("Referer"));
			
		}
		*/
	}
}
