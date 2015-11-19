package cse308.testscheduling.servlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cse308.testscheduling.Instructor;
import cse308.testscheduling.Student;
import cse308.testscheduling.User;

/**
 * Servlet implementation class CancelAppointmentServlet
 */
@WebServlet("/cancel_appt")
public class CancelAppointmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CancelAppointmentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int apptId = Integer.parseInt(request.getParameter("cancel"));
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userid");
		EntityManager em = DatabaseManager.createEntityManager();
		try {
			User user = em.find(User.class, userId);
			Student student = user.getStudent();
			if (student.cancelAppointment(em, apptId, false)) {
				session.setAttribute("user", user);
				request.getSession().setAttribute("message", "Successfully canceled appointment.");
			}
			else {
				request.getSession().setAttribute("message", "You cannot cancel this appointment.");
			}
		} catch (Exception e) {
			request.getSession().setAttribute("message", e);
		} finally {
			em.close();
			response.sendRedirect("ViewAppointments.jsp");
		}
	}


}
