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
import cse308.testscheduling.User;

/**
 * Servlet implementation class CancelRequestServlet
 */
@WebServlet("/cancel_request")
public class CancelRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CancelRequestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		String examId = request.getParameter("cancel");
		String userId = (String) session.getAttribute("userid");
		EntityManager em = DatabaseManager.createEntityManager();
		try {
			User user = em.find(User.class, userId);
			Instructor instructor = user.getInstructor();
			if (instructor.cancelRequest(em, examId)) {
				session.setAttribute("user", user);
				request.getSession().setAttribute("message", "Successfully canceled exam.");
			}
			else {
				request.getSession().setAttribute("message", "You cannot cancel this exam.");
			}
		} catch (Exception e) {
			request.getSession().setAttribute("message", e.getMessage());
		} finally {
			em.close();
			response.sendRedirect("ViewRequests.jsp");
		}
	}

}
