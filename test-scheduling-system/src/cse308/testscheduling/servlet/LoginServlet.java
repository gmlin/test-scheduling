package cse308.testscheduling.servlet;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cse308.testscheduling.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MakeRequestServlet.class.getName());

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
		File f = new File("/loginTest.log");
		FileHandler fh = new FileHandler("loginTest.log");
		logger.addHandler(fh);
		HttpSession session = request.getSession();
		session.setAttribute("username", request.getParameter("username"));
		session.setAttribute("password", request.getParameter("password"));
		EntityManager em = DatabaseManager.createEntityManager();

		try {
			User user = em.find(User.class, session.getAttribute("username"));
			if (user != null && user.getPassword().equals(session.getAttribute("password"))) {
				session.setAttribute("user", user);
				session.setAttribute("userid", user.getNetId());
				if (user.getAdministrator() != null) {
					logger.log(Level.INFO, "Login sucesfully as a Administrator: " + session.getAttribute("username"));
				}
				if (user.getStudent() != null) {
					logger.log(Level.INFO, "Login sucesfully as a Student: " + session.getAttribute("username"));
				}
				if (user.getInstructor() != null) {
					logger.log(Level.INFO, "Login sucesfully as a Instructor: " + session.getAttribute("username"));
				}
			}
			else {
				session.setAttribute("incorrect", true);
			}
		} catch (NoResultException e) {
			session.setAttribute("incorrect", true);
			logger.log(Level.SEVERE, "Login failure", e);

		} finally {
			response.sendRedirect("Login.jsp");
			em.close();
		}
	}

}
