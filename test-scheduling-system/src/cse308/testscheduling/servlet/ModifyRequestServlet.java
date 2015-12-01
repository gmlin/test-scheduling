package cse308.testscheduling.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
import cse308.testscheduling.Appointment;
import cse308.testscheduling.Exam;
import cse308.testscheduling.Status;
import cse308.testscheduling.Term;
import cse308.testscheduling.TestingCenter;
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
			String msg = "";
			int numapproved = 0;
			while (examIds.hasMoreElements()) {
				String examId = examIds.nextElement();
				if (!examId.equals("OWASP_CSRFTOKEN")) {
					if (request.getParameter(examId).equals("approve")) {
						if (numapproved == 0) {
							Query query = em.createQuery("SELECT term FROM Term term WHERE term.current = true");
							Term term = (Term) query.getResultList().get(0);
							TestingCenter tc = term.getTestingCenter();
							if (tc.isSchedulable(em, examId)) {
								admin.modifyRequest(em, examId, Status.APPROVED);
								logger.log(Level.INFO, examId + " has been approved by admin");
								msg += examId + " has been approved by admin.<br>";
								numapproved++;
							}
							else {
								throw new Exception("This exam is not schedulable.");
							}
						}
						else {
							msg += "You may only approve one exam at a time.<br>";
						}
					} else {
						admin.modifyRequest(em, examId, Status.DENIED);
						logger.log(Level.INFO, examId + " has been denied by admin");
						msg += examId + " has been denied by admin.<br>";
					}
				}
			}
			session.setAttribute("user", user);
			request.getSession().setAttribute("message", msg);
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("message", e.getMessage());
			logger.log(Level.SEVERE, "Error in approving/denying exam request", e);
		} finally {
			em.close();
			response.sendRedirect(request.getHeader("Referer"));
			logger.exiting(getClass().getName(), "AcceptRejectRequest");
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String examu = request.getParameter("examu");
		EntityManager em = DatabaseManager.createEntityManager();
		Exam exam = em.find(Exam.class, examu);
		Term term = exam.getTerm();
		try {
			LocalDate startD = exam.getStartDateTime().toLocalDateTime().toLocalDate();
			LocalDate endD = exam.getEndDateTime().toLocalDateTime().toLocalDate();
			TreeMap<LocalDate, Double> futuredays = new TreeMap<LocalDate, Double>();
			TreeMap<LocalDate, Double> futuredays2 = new TreeMap<LocalDate, Double>();
			List<String> future = new ArrayList<String>();
			List<String> future2 = new ArrayList<String>();
			for (LocalDate date = startD; !date.isAfter(endD); date = date.plusDays(1)) {
				Double actual = 0.0;
				Double expected = 0.0;
				Double expected2 = 0.0;
				int totalDuration = 0;
				Query query = em.createQuery("SELECT appt FROM Appointment appt WHERE appt.dateTime > :t1 AND appt.dateTime < :t2");
				query.setParameter("t1", Timestamp.valueOf(date.atStartOfDay()));
				query.setParameter("t2", Timestamp.valueOf(date.plusDays(1).atStartOfDay()));
				List<Appointment> appts = query.getResultList();
				for (Appointment appt: appts) {
					totalDuration += ((appt.getExam().getDuration() + term.getTestingCenter().getGapTime() + 29) / 30) * 30;
				}
				actual = (double)totalDuration/(
						((double)term.getTestingCenter().getNumSeats() + (double)term.getTestingCenter().getNumSetAsideSeats())
						* (double)term.getTestingCenter().getTotalOpenTime());
				Double expectedApptDuration = 0.0;
				Double expectedApptDuration2 = 0.0;
				Query query2 = em.createQuery("SELECT exam FROM Exam exam WHERE exam.startDateTime < :t1 AND exam.endDateTime > :t2 AND (exam.status = :s1 OR exam.status = :s2)");
				query2.setParameter("t1", Timestamp.valueOf(date.plusDays(1).atStartOfDay()));
				query2.setParameter("t2", Timestamp.valueOf(date.atStartOfDay()));
				query2.setParameter("s1", Status.APPROVED);
				query2.setParameter("s2", Status.ONGOING);
				List<Exam> exams = query2.getResultList();
				List<Exam> exams2 = query2.getResultList();
				exams2.add(exam);
				for (Exam e: exams) {
					if (e.getAdHoc()) {
						expectedApptDuration += (((double)e.getDuration() + (double)term.getTestingCenter().getGapTime())
								* ((double)e.getStudents().size() - (double)e.getAppointments().size()) 
								/ (double)e.getDays());
					} else {
						expectedApptDuration += (((double)e.getDuration() + (double)term.getTestingCenter().getGapTime())
								* ((double)e.getCourse().getStudents().size() - (double)e.getAppointments().size()) 
								/ (double)e.getDays());
					}
				}
				expected = (actual + expectedApptDuration / (
						((double)term.getTestingCenter().getNumSeats() + (double)term.getTestingCenter().getNumSetAsideSeats())
						* (double)term.getTestingCenter().getTotalOpenTime()));	
				futuredays.put(date, expected);
				
				for (Exam e: exams2) {
					if (e.getAdHoc()) {
						expectedApptDuration2 += (((double)e.getDuration() + (double)term.getTestingCenter().getGapTime())
								* ((double)e.getStudents().size() - (double)e.getAppointments().size()) 
								/ (double)e.getDays());
					} else {
						expectedApptDuration2 += (((double)e.getDuration() + (double)term.getTestingCenter().getGapTime())
								* ((double)e.getCourse().getStudents().size() - (double)e.getAppointments().size()) 
								/ (double)e.getDays());
					}
				}
				expected2 = (actual + expectedApptDuration2 / (
						((double)term.getTestingCenter().getNumSeats() + (double)term.getTestingCenter().getNumSetAsideSeats())
						* (double)term.getTestingCenter().getTotalOpenTime()));	
				futuredays2.put(date, expected2);
			}
			for (Map.Entry<LocalDate, Double> entry: futuredays.entrySet()) {
				future.add(entry.getKey() + ": " + entry.getValue() + "<br></br>");
			}
			for (Map.Entry<LocalDate, Double> entry: futuredays2.entrySet()) {
				future2.add(entry.getKey() + ": " + entry.getValue() + "<br></br>");
			}
			String formatedfuture = future.toString()
				    .replace(",", "") 
				    .replace("[", "") 
				    .replace("]", "")  
				    .trim();
			String formatedfuture2 = future2.toString()
				    .replace(",", "") 
				    .replace("[", "") 
				    .replace("]", "")  
				    .trim();
			session.setAttribute("report", "The current expected utilization from " + startD + " to " + endD + ":<br></br>"
					+ formatedfuture 
					+ "<br></br>The expected utilization from " + startD + " to " + endD + " if exam " + exam.getExamId() + " is approved:<br></br>"
					+ formatedfuture2);
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", e);
		} finally {
			em.close();
			response.sendRedirect(request.getHeader("Referer"));
		}
	}
}
