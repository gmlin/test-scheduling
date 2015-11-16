package cse308.testscheduling.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cse308.testscheduling.Administrator;
import cse308.testscheduling.Appointment;
import cse308.testscheduling.Exam;
import cse308.testscheduling.Seat;
import cse308.testscheduling.Student;
import cse308.testscheduling.User;

/**
 * Servlet implementation class AppointmentServlet
 */
@WebServlet("/make_appointment")
public class MakeAppointmentServlet extends HttpServlet {

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
		HttpSession s = request.getSession();

		String examId = request.getParameter("exam");
		Timestamp dateTime = Timestamp.valueOf(request.getParameter("dateTime").replace("T", " ") + ":00");
		Appointment appt = null;
		try {
			if (request.getParameter("appt_type").equals("admin")) {
				Administrator admin = ((User)(s.getAttribute("user"))).getAdministrator();
				String studentId = (String) request.getAttribute("student");
				boolean setAside;
				if (request.getAttribute("setAside").equals("yes"))
					setAside = true;
				else
					setAside = false;
				appt = admin.makeAppointment(studentId, examId, dateTime, setAside);
			} else if (request.getParameter("appt_type").equals("student")) {
				Student student = ((User)(s.getAttribute("user"))).getStudent();
				appt = student.makeAppointment(examId, dateTime, false);
			}	
			if (appt == null) {
				request.getSession().setAttribute("message", "Failed to schedule appointment.");
			} else {
				request.getSession().setAttribute("message", "Successfully scheduled appointment.");
			}
		} catch (Exception e) {
			request.getSession().setAttribute("message", e);
		} finally {
			response.sendRedirect(request.getHeader("Referer"));
		}
	}

}
