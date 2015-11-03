package cse308.testscheduling;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.FileHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ApproveRequestServlet
 */
@WebServlet("/modify_request")
public class ModifyRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyRequestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String examId = request.getParameter("cancel");
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-scheduling-system");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Query query;
		HttpSession session = request.getSession();
		try {
			query = em.createQuery("SELECT e FROM Exam e WHERE e.examId = :examId", Exam.class);
			query.setParameter("examId", examId);
			Exam exam = (Exam)query.getSingleResult();
			if (exam.getStatus() == Status.PENDING) {
				Instructor instructor = ((User)session.getAttribute("user")).getInstructor();
				if (exam.hasPermissions(instructor)) {
					em.remove(exam);
					request.getSession().setAttribute("message", "Successfully canceled request.");
					em.getTransaction().commit();
					request.getSession().setAttribute("user", em.find(User.class, ((User)session.getAttribute("user")).getNetId()));
				}
				else {
					request.getSession().setAttribute("message", "You are not permitted to delete this exam.");
				}
			}
			else {
				request.getSession().setAttribute("message", "You are not permitted to delete this exam.");
			}
		}
		catch (Exception e) {
			request.getSession().setAttribute("message", e.toString());
		}
		finally {
			em.close();
			emf.close();
			response.sendRedirect("ViewRequests.jsp");
		}
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Enumeration<String> examIds = request.getParameterNames();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-scheduling-system");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Query query;
		try {
			while (examIds.hasMoreElements()) {
				String examId = examIds.nextElement();
				query = em.createQuery("SELECT e FROM Exam e WHERE e.examId = :examId", Exam.class);
				query.setParameter("examId", examId);
				Exam exam = (Exam)query.getSingleResult();
				if (request.getParameter(examId).equals("approve")) {
					exam.setStatus(Status.APPROVED);
				}
				else {
					exam.setStatus(Status.DENIED);
				}
			}
			em.getTransaction().commit();
		}
		catch(Exception e) {
			request.getSession().setAttribute("message", e.toString());
		}
		finally {
			em.close();
			emf.close();
			response.sendRedirect(request.getHeader("Referer"));
		}
	}
}
