package cse308.testscheduling;


import java.io.IOException;

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
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		session.setAttribute("username", request.getParameter("username"));
		session.setAttribute("password", request.getParameter("password"));
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-scheduling-system");
	    EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
	    query.setParameter("username", session.getAttribute("username"));
	    query.setParameter("password", session.getAttribute("password")); 
	    try{ 
	    	User user = (User) query.getSingleResult();
	    	em.persist(user);
	    	session.setAttribute("account", user);
	    }
	    catch (NoResultException e) {
	    	session.setAttribute("incorrect", true);
	    	
	    }
	    finally {
	    	response.sendRedirect("Login.jsp");
	    }
	}

}