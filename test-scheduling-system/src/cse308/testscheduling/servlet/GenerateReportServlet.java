package cse308.testscheduling.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cse308.testscheduling.Appointment;
import cse308.testscheduling.Course;
import cse308.testscheduling.Exam;
import cse308.testscheduling.Term;

@WebServlet("/generate_report")
public class GenerateReportServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public GenerateReportServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String report = request.getParameter("report");
		HttpSession session = request.getSession();
		EntityManager em = DatabaseManager.createEntityManager();
		Term term = em.find(Term.class, Integer.parseInt(request.getParameter("termID")));
		Term termStart = em.find(Term.class, Integer.parseInt(request.getParameter("termStart")));
		Term termEnd = em.find(Term.class, Integer.parseInt(request.getParameter("termEnd")));
		try {
			if (report == null) {
			    session.setAttribute("message", "No report selected");
			    
			} else if (report.equals("daily")) {
			    session.setAttribute("message", "Daily Appointment Report<br></br>" 
			    		+ term);
			    
			} else if (report.equals("weekly")) {
			    session.setAttribute("message", "Weekly Appointment Report<br></br>"
			    		+ term );
			    
			} else if (report.equals("term")) {
				List<Course> courses = new ArrayList<Course>();
				for (Course c: term.getCourses()) {
					if (!(c.getExams().isEmpty())) {
						courses.add(c);
					}
				}
				session.setAttribute("message", "Term Course Report<br></br>"
						+ "Courses that have used the testing center in term " + term + ":<br></br>"
						+ courses);
			} else if (report.equals("termrange")) {
				if (termEnd.getTermID() < termStart.getTermID())
					session.setAttribute("message", "Please enter a valid term range");
				else {
					Query query = em.createQuery("SELECT term FROM Term term");
					List<Term> termsInRange = new ArrayList<Term>();
					List<Term> terms = query.getResultList();
					List<String> termsTotal = new ArrayList<String>();
					for (Term t: terms) {
						if ((t.getTermID() >= termStart.getTermID()) && (t.getTermID() <= termEnd.getTermID())) {
							termsInRange.add(t);
						}
					}
					for (Term t: termsInRange) {
						int totalappointments = 0;
						for (Course c: t.getCourses()) {
							for (Exam e: c.getExams()) {
								for (Appointment a: e.getAppointments()) {
									totalappointments++;
								}
							}
						}
						termsTotal.add("Term " + t + " has " + totalappointments + " student appointments");
					}
					session.setAttribute("message", "Term Range Appointment Report<br></br>"
							+ "Total number of student appointments in each term between term " + termStart +" and term " + termEnd + ":<br></br>"
							+ termsTotal);
				}
			} else {
				session.setAttribute("message", "Invalid report selected");
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", e);
		} finally {
			em.close();
			response.sendRedirect("GenerateReports.jsp");
		}
	}
}
