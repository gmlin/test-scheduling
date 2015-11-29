package cse308.testscheduling.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
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
		String[] split = range.split(" - ", 2);
		String start = split[0];
		String end = split[1];
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date startDate = format.parse(start);
			Date endDate = format.parse(end);
			TreeMap<LocalDate, Double> days = new TreeMap<LocalDate, Double>();
			LocalDate startD = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate endD = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			for (LocalDate date = startD; !date.isAfter(endD); date = date.plusDays(1)) {
				Double actual = 0.0;
				Double expected = 0.0;
				int totalduration = 0;
				Query query = em.createQuery("SELECT appt FROM Appointment appt WHERE appt.dateTime > :t1 AND appt.dateTime < :t2");
				query.setParameter("t1", Timestamp.valueOf(date.atStartOfDay()));
				query.setParameter("t2", Timestamp.valueOf(date.plusDays(1).atStartOfDay()));
				List<Appointment> appts = query.getResultList();
				for (Appointment appt: appts) {
					//get totalduration by adding up duration of every exam
					//(duration + gap time + time rounded up to next timeslot)
					
				}
				if (!(date.isAfter(LocalDate.now()))) {
					//actual utilization for current and previous days
					days.put(date, actual);
				} else {
					//expected utilization for future days
					days.put(date, expected);
				}
			}
			session.setAttribute("message", "The utilization for each day in the date range " + range + ":<br></br>"
					+ days);
		} catch (Exception e) {
			session.setAttribute("message", e);
		} finally {
			em.close();
			response.sendRedirect("Utilization.jsp");
		}
	}
}
