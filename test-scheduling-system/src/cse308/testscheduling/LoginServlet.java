package cse308.testscheduling;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ScheduleExamServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		File f = new File("/loginTest.txt");
		FileHandler fh = new FileHandler("loginTest.txt");
		logger.addHandler(fh);
		HttpSession session = request.getSession();
		session.setAttribute("username", request.getParameter("username"));
		session.setAttribute("password", request.getParameter("password"));
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-scheduling-system");
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT u FROM User u WHERE u.netId = :username AND u.password = :password",
				User.class);
		query.setParameter("username", session.getAttribute("username"));
		query.setParameter("password", session.getAttribute("password"));
		try {
			User user = (User) query.getSingleResult();
			session.setAttribute("user", user);
			if(user.getAdministrator()!= null){
				logger.log(Level.SEVERE, "Login sucesfully as a Administrator: " + session.getAttribute("username"));
			}
			if(user.getStudent()!= null){
				logger.log(Level.SEVERE, "Login sucesfully as a Student: " + session.getAttribute("username"));
			}
			if(user.getInstructor()!= null){
				logger.log(Level.SEVERE, "Login sucesfully as a Instructor: " + session.getAttribute("username"));
			}
		} catch (NoResultException e) {
			session.setAttribute("incorrect", true);
			logger.log(Level.SEVERE, "Login failure", e);
			

		} finally {
			response.sendRedirect("Login.jsp");
			em.close();
			emf.close();
		}
	}

}
