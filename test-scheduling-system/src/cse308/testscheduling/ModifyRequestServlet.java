package cse308.testscheduling;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private static final Logger logger = Logger.getLogger(ModifyRequestServlet.class.getName());
       
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
			logger.entering(getClass().getName(), "doGet");
			File f = new File("/CancelRequest.txt");
			FileHandler fh = null;
			try {
				fh = new FileHandler("CancelRequest.txt");
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			logger.addHandler(fh);
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
					logger.log(Level.INFO, instructor.getUser().getFirstName() + " " + instructor.getUser().getLastName() +
							" has sucessfully canceled: " + exam.getExamId());
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
			logger.log(Level.SEVERE, "Error in canceling exam request", e);
		}
		finally {
			em.close();
			emf.close();
			response.sendRedirect("ViewRequests.jsp");
			logger.exiting(getClass().getName(), "CancelRequest");
		}
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Enumeration<String> examIds = request.getParameterNames();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-scheduling-system");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Query query;
		try {
			logger.entering(getClass().getName(), "doGet");
			File f = new File("/AcceptRejectRequest.txt");
			FileHandler fh = null;
			try {
				fh = new FileHandler("AcceptRejectRequest.txt");
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			logger.addHandler(fh);
			while (examIds.hasMoreElements()) {
				String examId = examIds.nextElement();
				query = em.createQuery("SELECT e FROM Exam e WHERE e.examId = :examId", Exam.class);
				query.setParameter("examId", examId);
				Exam exam = (Exam)query.getSingleResult();
				if (request.getParameter(examId).equals("approve")) {
					exam.setStatus(Status.APPROVED);
					logger.log(Level.INFO, exam.getExamId() + " has been approved by admin");
				}
				else {
					exam.setStatus(Status.DENIED);
					logger.log(Level.INFO, exam.getExamId() + " has been denied by admin");
				}
			}
			em.getTransaction().commit();
		}
		catch(Exception e) {
			request.getSession().setAttribute("message", e.toString());
			logger.log(Level.SEVERE, "Error in approving/denying exam request", e);
		}
		finally {
			em.close();
			emf.close();
			response.sendRedirect(request.getHeader("Referer"));
			logger.exiting(getClass().getName(), "AcceptRejectRequest");
		}
	}
}
