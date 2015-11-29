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

import cse308.testscheduling.Administrator;
import cse308.testscheduling.Appointment;
import cse308.testscheduling.User;

/**
 * Servlet implementation class ModifyAppointmentServlet
 */
@WebServlet("/modify_appt")
public class ModifyAppointmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyAppointmentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManager em = DatabaseManager.createEntityManager();
		HttpSession session = request.getSession();
		if (request.getParameter("cancel") != null) {
			int apptId = Integer.parseInt(request.getParameter("cancel"));
			String userId = (String) session.getAttribute("userid");
			try {
				User user = em.find(User.class, userId);
				Administrator admin = user.getAdministrator();
				if (admin.cancelAppointment(em, apptId)) {
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
				response.sendRedirect("AdminViewAppointments.jsp");
			}
		}
		else {
			int apptId = Integer.parseInt(request.getParameter("appt"));
			try {
				Appointment appt = em.find(Appointment.class, apptId);
				session.setAttribute("appt", appt);
			}
			catch (Exception e) {
				request.getSession().setAttribute("message", e.getMessage());
			}
			finally {
				em.close();
				response.sendRedirect("ModifyAppointment.jsp");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userid");
		EntityManager em = DatabaseManager.createEntityManager();
		int apptId = Integer.parseInt(request.getParameter("apptId"));
		Timestamp dateTime = Timestamp.valueOf(request.getParameter("dateTime"));
		int seatNumber = Integer.parseInt(request.getParameter("seat"));
		boolean attendance = request.getParameter("attended").equals("yes");
		try {
			User user = em.find(User.class, userId);
			Administrator admin = user.getAdministrator();
			if (admin.modifyAppointment(em, apptId, dateTime, seatNumber, attendance)) {
				session.setAttribute("user", user);
				request.getSession().setAttribute("message", "Appointment modified.");
			}
			else {
				request.getSession().setAttribute("message", "Cannot change to this date/time or seat.");
			}
		} catch (Exception e) {
			request.getSession().setAttribute("message", e.getMessage());
		} finally {
			em.close();
			response.sendRedirect("modify_appt?appt=" + apptId);
		}
	}

}
