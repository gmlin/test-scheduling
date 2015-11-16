package cse308.testscheduling.servlet;

import java.io.IOException;
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
@WebServlet("/cancel_appointment")
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
		String apptId = request.getParameter("cancel");
		HttpSession session = request.getSession();
		Student student = ((User) session.getAttribute("user")).getStudent();
		try {
			if (student.cancelAppointment(apptId)) {
				request.getSession().setAttribute("message", "Successfully canceled appointment.");
			}
			else {
				request.getSession().setAttribute("message", "You cannot cancel this appointment.");
			}
		} catch (Exception e) {
			request.getSession().setAttribute("message", e);
		} finally {
			response.sendRedirect("ViewAppointments.jsp");
		}
	}


}
