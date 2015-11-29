package cse308.testscheduling.servlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cse308.testscheduling.Administrator;
import cse308.testscheduling.User;

/**
 * Servlet implementation class CheckInServlet
 */
@WebServlet("/check_in")
public class CheckInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckInServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userid");
		String studentId = (String)request.getParameter("student");
		EntityManager em = DatabaseManager.createEntityManager();
		try {
			User user = em.find(User.class, userId);
			Administrator admin = user.getAdministrator();
			int seatNumber = admin.checkIn(em, studentId);
			if (seatNumber != -1) {
				session.setAttribute("user", user);
				request.getSession().setAttribute("message", "Check in successful. Seat number is " + seatNumber + ".");
			}
			else {
				request.getSession().setAttribute("message", "No appointment found for student.");
			}
		} catch (Exception e) {
			request.getSession().setAttribute("message", e.getMessage());
		} finally {
			em.close();
			response.sendRedirect("CheckIn.jsp");
		}
	}

}
