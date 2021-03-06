package cse308.testscheduling.servlet;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cse308.testscheduling.Administrator;
import cse308.testscheduling.Appointment;
import cse308.testscheduling.Student;
import cse308.testscheduling.User;

/**
 * Servlet implementation class AppointmentServlet
 */
@SuppressWarnings("deprecation")
@WebServlet("/make_appointment")
public class MakeAppointmentServlet extends HttpServlet implements SingleThreadModel {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MakeAppointmentServlet() {
		super();
		// TODO Auto-generated constructor stub
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
		String examId = request.getParameter("exam");
		Timestamp dateTime = Timestamp.valueOf(request.getParameter("dateTime"));
		Appointment appt = null;
		EntityManager em = DatabaseManager.createEntityManager();
		try {
			User user = em.find(User.class, userId);
			if (request.getParameter("appt_type").equals("admin")) {
				Administrator admin = user.getAdministrator();
				String studentId = (String) request.getParameter("student");
				boolean setAside;
				if (request.getParameter("setAside").equals("yes"))
					setAside = true;
				else
					setAside = false;
				appt = admin.makeAppointment(em, studentId, examId, dateTime, setAside);
			} else if (request.getParameter("appt_type").equals("student")) {
				Student student = user.getStudent();
				appt = student.makeAppointment(em, examId, dateTime, false);
			}	
			if (appt == null) {
				session.setAttribute("message", "Cannot schedule appointment.");
			} else {
				session.setAttribute("user", user);
				session.setAttribute("message", "Successfully scheduled appointment.");
			}
		} catch (Exception e) {
			session.setAttribute("message", e.getMessage());
		} finally {
			em.close();
			response.sendRedirect(request.getHeader("Referer"));
		}
	}

}
