package cse308.testscheduling.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cse308.testscheduling.Appointment;
import cse308.testscheduling.Exam;
import cse308.testscheduling.Term;
import cse308.testscheduling.Status;

@WebServlet("/utilization")
public class UtilizationServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public UtilizationServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		EntityManager em = DatabaseManager.createEntityManager();
		String range = request.getParameter("utilizationDateRange");
		Term term = em.find(Term.class, Integer.parseInt(request.getParameter("termID")));
		String[] split = range.split(" - ", 2);
		String start = split[0];
		String end = split[1];
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date startDate = format.parse(start);
			Date endDate = format.parse(end);
			TreeMap<LocalDate, Double> pastcurrentdays = new TreeMap<LocalDate, Double>();
			TreeMap<LocalDate, Double> futuredays = new TreeMap<LocalDate, Double>();
			LocalDate startD = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate endD = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			for (LocalDate date = startD; !date.isAfter(endD); date = date.plusDays(1)) {
				Double actual = 0.0;
				Double expected = 0.0;
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
				if (!(date.isAfter(LocalDate.now()))) {
					pastcurrentdays.put(date, actual);
				} else {
					Double expectedApptDuration = 0.0;
					Query query2 = em.createQuery("SELECT exam FROM Exam exam WHERE exam.startDateTime < :t1 AND exam.endDateTime > :t2 AND (exam.status = :s1 OR exam.status = :s2)");
					query2.setParameter("t1", Timestamp.valueOf(date.plusDays(1).atStartOfDay()));
					query2.setParameter("t2", Timestamp.valueOf(date.atStartOfDay()));
					query2.setParameter("s1", Status.APPROVED);
					query2.setParameter("s2", Status.ONGOING);
					List<Exam> exams = query2.getResultList();
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
				}
			}
			session.setAttribute("message", "The utilization for each day in the date range " + range + ":<br></br><br></br>"
					+ "Actual utilization of past and current days:<br></br>" + pastcurrentdays + "<br></br>"
					+ "Expected utilization of future days:<br></br>" + futuredays);
		} catch (Exception e) {
			session.setAttribute("message", e);
		} finally {
			em.close();
			response.sendRedirect("Utilization.jsp");
		}
	}
}
