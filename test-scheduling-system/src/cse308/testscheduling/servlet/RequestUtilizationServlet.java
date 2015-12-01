package cse308.testscheduling.servlet;

import java.io.IOException;
import java.sql.Timestamp;

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
 * Servlet implementation class RequestUtilizationServlet
 */
@WebServlet("/request_utilization")
public class RequestUtilizationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequestUtilizationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userid");
		EntityManager em = DatabaseManager.createEntityManager();
		int duration = Integer.parseInt(request.getParameter("examDuration"));
		session.setAttribute("duration", duration);
		Timestamp startDateTime = Timestamp.valueOf(request.getParameter("startDateTime"));
		Timestamp endDateTime = Timestamp.valueOf(request.getParameter("endDateTime"));
		session.setAttribute("startDateTime", startDateTime);
		session.setAttribute("endDateTime", endDateTime);
		session.setAttribute("exam_type", request.getParameter("exam_type"));
		if (request.getParameter("exam_type").equals("course")) {
			String courseId = request.getParameter("courseId");
			session.setAttribute("courseId", courseId);
		} else if (request.getParameter("exam_type").equals("adhoc")) {
			String netIds = request.getParameter("netids");
			session.setAttribute("netIds", netIds);
		}
		// INSERT CODE FOR UTILIZATION HERE
		session.setAttribute("requestexam", "yes");
		response.sendRedirect("InstructorUtilization.jsp");
	}

}
