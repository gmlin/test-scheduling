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

import cse308.testscheduling.Administrator;
import cse308.testscheduling.TestingCenter;
import cse308.testscheduling.User;

/**
 * Servlet implementation class EditTestingCenterServlet
 */
@WebServlet("/edit_testing_center_info")
public class EditTestingCenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditTestingCenterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		EntityManager em = DatabaseManager.createEntityManager();
		String userId = (String) session.getAttribute("userid");
		try{
			User user = em.find(User.class, userId);
			Administrator admin = user.getAdministrator();
			int numSeats = Integer.parseInt(request.getParameter("numSeats"));
			int numSetAside = Integer.parseInt(request.getParameter("numSetAside"));
			String timestring = request.getParameter("openTime");
			String[] ts = timestring.split(":");
			String timestring2 = request.getParameter("closeTime");
			String[] ts2 = timestring2.split(":");
			Timestamp openTime = new Timestamp(0, 0, 0, Integer.parseInt(ts[0]), Integer.parseInt(ts[1]), 0, 0);
			Timestamp closeTime = new Timestamp(0, 0, 0, Integer.parseInt(ts2[0]), Integer.parseInt(ts2[1]), 0, 0);
			int gapTime = Integer.parseInt(request.getParameter("gapTime"));
			int reminderInterval = Integer.parseInt(request.getParameter("reminderInterval"));
			admin.modifyTestingCenter(em, numSeats, numSetAside, openTime, closeTime, gapTime, reminderInterval);
			session.setAttribute("user", user);
			session.setAttribute("message", "Testing Center has been updated");
		} catch (Exception e) {
			session.setAttribute("message", e.getMessage());
		} finally {
			em.close();
			response.sendRedirect(request.getHeader("Referer"));
		}
		
		
	}

}
