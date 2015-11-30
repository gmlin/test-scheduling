package cse308.testscheduling.servlet;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
				TreeMap<Date, Integer> days = new TreeMap<Date, Integer>();
				for (Course c: term.getCourses()) {
					for (Exam e: c.getExams()) {
						for (Appointment a: e.getAppointments()) {
							Timestamp t = a.getDateTime();
							Date d = new Date(t.getYear(), t.getMonth(), t.getDate());
							if (!(days.containsKey(d))) {
								days.put(d,1);
							} else {
								Integer i = days.get(d)+1;
								days.put(d,i);
							}
						}
					}
				}
				Query query = em.createQuery("SELECT exam FROM Exam exam WHERE exam.adHoc = true");
				List<Exam> adHocs = query.getResultList();
				for (Exam e: adHocs) {
					if (e.getTerm().getTermID() == term.getTermID()) {
						for (Appointment a: e.getAppointments()) {
							Timestamp t = a.getDateTime();
							Date d = new Date(t.getYear(), t.getMonth(), t.getDate());
							if (!(days.containsKey(d))) {
								days.put(d,1);
							} else {
								Integer i = days.get(d)+1;
								days.put(d,i);
							}
						}
					}
				}
				List<String> day = new ArrayList<String>();
				for (Map.Entry<Date, Integer> entry: days.entrySet()) {
					day.add(entry.getKey() + ": " + entry.getValue() + "<br></br>");
				}
				String formatedday = day.toString()
					    .replace(",", "") 
					    .replace("[", "") 
					    .replace("]", "")  
					    .trim();
			    session.setAttribute("message", "Daily Appointment Report<br></br>" 
			    		+ "Number of student appointments on each day of term " + term + ":<br></br>"
			    		+ formatedday + "(Days not listed have no appointments)");
			    
			} else if (report.equals("weekly")) {
			    session.setAttribute("message", "Weekly Appointment Report<br></br>"
			    		+ "Number of student appointments on each week of term " + term + " (with course identifiers):<br></br>"
			    		);
			    
			} else if (report.equals("term")) {
				List<Course> courses = new ArrayList<Course>();
				for (Course c: term.getCourses()) {
					if (!(c.getExams().isEmpty())) {
						courses.add(c);
					}
				}
				List<String> course = new ArrayList<String>();
				for (Course c: courses) {
					course.add(c.toString());
				}
				String formatedcourse = course.toString()
					    .replace("[", "") 
					    .replace("]", "")  
					    .trim();
				session.setAttribute("message", "Term Course Report<br></br>"
						+ "Courses that have used the testing center in term " + term + ":<br></br>"
						+ formatedcourse);
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
								totalappointments += e.getAppointments().size();
							}
						}
						Query query2 = em.createQuery("SELECT exam FROM Exam exam WHERE exam.adHoc = true");
						List<Exam> adHocs = query2.getResultList();
						for (Exam e: adHocs) {
							if (e.getTerm().getTermID() == t.getTermID()) {
								totalappointments += e.getAppointments().size();
							}
						}
						termsTotal.add("Term " + t + " has " + totalappointments + " student appointments<br></br>");
					}
					String formatedtotal = termsTotal.toString()
						    .replace(",", "") 
						    .replace("[", "") 
						    .replace("]", "")  
						    .trim();
					session.setAttribute("message", "Term Range Appointment Report<br></br>"
							+ "Number of student appointments in each term between term " + termStart +" and term " + termEnd + ":<br></br>"
							+ formatedtotal);
				}
			} else {
				session.setAttribute("message", "Invalid report selected");
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", e.getMessage());
		} finally {
			em.close();
			response.sendRedirect("GenerateReports.jsp");
		}
	}
}
