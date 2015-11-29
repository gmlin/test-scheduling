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
 * Servlet implementation class SetTermServlet
 */
@WebServlet("/set_term")
public class SetTermServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetTermServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		EntityManager em = DatabaseManager.createEntityManager();
		String userId = (String) session.getAttribute("userid");
		try{
			User user = em.find(User.class, userId);
			Administrator admin = user.getAdministrator();			
			int termId = Integer.parseInt(request.getParameter("termID"));
			admin.setCurrentTerm(em, termId);
			session.setAttribute("user", user);
			session.setAttribute("message", "Term has been set.");
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", e.getMessage());
		} finally {
			em.close();
			response.sendRedirect(request.getHeader("Referer"));
		}
		
	}

}
