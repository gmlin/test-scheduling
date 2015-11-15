package cse308.testscheduling.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cse308.testscheduling.DatabaseManager;
import cse308.testscheduling.Exam;
import cse308.testscheduling.Instructor;
import cse308.testscheduling.Status;
import cse308.testscheduling.User;

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

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String examId = request.getParameter("cancel");
		EntityManager em = DatabaseManager.createEntityManager();
		em.getTransaction().begin();
		HttpSession session = request.getSession();
		try {
			logger.entering(getClass().getName(), "doGet");
			File f = new File("/CancelRequest.log");
			FileHandler fh = null;
			try {
				fh = new FileHandler("CancelRequest.log");
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			logger.addHandler(fh);
			Exam exam = em.find(Exam.class, examId);

			if (exam.getStatus() == Status.PENDING) {
				Instructor instructor = ((User) session.getAttribute("user")).getInstructor();
				if (exam.hasPermissions(instructor)) {
					em.remove(exam);
					request.getSession().setAttribute("message", "Successfully canceled request.");
					em.getTransaction().commit();
					request.getSession().setAttribute("user",
							em.find(User.class, ((User) session.getAttribute("user")).getNetId()));
					logger.log(Level.INFO, instructor.getUser().getFirstName() + " "
							+ instructor.getUser().getLastName() + " has sucessfully canceled: " + exam.getExamId());
				} else {
					request.getSession().setAttribute("message", "You are not permitted to delete this exam.");
				}
			} else {
				request.getSession().setAttribute("message", "You are not permitted to delete this exam.");
			}
		} catch (Exception e) {
			request.getSession().setAttribute("message", e.toString());
			logger.log(Level.SEVERE, "Error in canceling exam request", e);
		} finally {
			em.close();
			response.sendRedirect("ViewRequests.jsp");
			logger.exiting(getClass().getName(), "CancelRequest");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Enumeration<String> examIds = request.getParameterNames();
		EntityManager em = DatabaseManager.createEntityManager();
		em.getTransaction().begin();
		try {
			logger.entering(getClass().getName(), "doGet");
			File f = new File("/AcceptRejectRequest.log");
			FileHandler fh2 = null;
			try {
				fh2 = new FileHandler("AcceptRejectRequest.log");
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			logger.addHandler(fh2);
			while (examIds.hasMoreElements()) {
				String examId = examIds.nextElement();
				Exam exam = em.find(Exam.class, examId);
				if (request.getParameter(examId).equals("approve")) {
					exam.setStatus(Status.APPROVED);
					logger.log(Level.INFO, exam.getExamId() + " has been approved by admin");
				} else {
					exam.setStatus(Status.DENIED);
					logger.log(Level.INFO, exam.getExamId() + " has been denied by admin");
				}
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			request.getSession().setAttribute("message", e.toString());
			logger.log(Level.SEVERE, "Error in approving/denying exam request", e);
		} finally {
			em.close();
			response.sendRedirect(request.getHeader("Referer"));
			logger.exiting(getClass().getName(), "AcceptRejectRequest");
		}
	}
}
