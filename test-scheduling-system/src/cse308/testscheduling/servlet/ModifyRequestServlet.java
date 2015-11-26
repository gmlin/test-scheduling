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

import cse308.testscheduling.Administrator;
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Enumeration<String> examIds = request.getParameterNames();
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userid");
		EntityManager em = DatabaseManager.createEntityManager();
		try {
			logger.entering(getClass().getName(), "doPost");
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
			User user = em.find(User.class, userId);
			Administrator admin = user.getAdministrator();
			if (examIds.hasMoreElements()) {
				examIds.nextElement();
			}
			while (examIds.hasMoreElements()) {
				String examId = examIds.nextElement();
				System.out.println("Exam id is (" + examId + ")");
				if (request.getParameter(examId).equals("approve")) {
					admin.modifyRequest(em, examId, Status.APPROVED);
					logger.log(Level.INFO, examId + " has been approved by admin");
				} else {
					admin.modifyRequest(em, examId, Status.DENIED);
					logger.log(Level.INFO, examId + " has been denied by admin");
				}
			}
			session.setAttribute("user", user);
			request.getSession().setAttribute("message", "Exams successfully approved/denied.");
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("message", e.toString());
			logger.log(Level.SEVERE, "Error in approving/denying exam request", e);
		} finally {
			em.close();
			response.sendRedirect(request.getHeader("Referer"));
			logger.exiting(getClass().getName(), "AcceptRejectRequest");
		}
	}
}
