package cse308.testscheduling.servlet;

import java.io.IOException;
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
import cse308.testscheduling.Instructor;
import cse308.testscheduling.Status;
import cse308.testscheduling.Term;
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
		String courseId = null;
		String netIds = null;
		if (request.getParameter("exam_type").equals("course")) {
			courseId = request.getParameter("courseId");
			session.setAttribute("courseId", courseId);
		} else if (request.getParameter("exam_type").equals("adhoc")) {
			netIds = request.getParameter("netids");
			session.setAttribute("netIds", netIds);
		}
		try {
			LocalDate startD = startDateTime.toLocalDateTime().toLocalDate();
			LocalDate endD = endDateTime.toLocalDateTime().toLocalDate();
			TreeMap<LocalDate, Double> futuredays = new TreeMap<LocalDate, Double>();
			TreeMap<LocalDate, Double> futuredays2 = new TreeMap<LocalDate, Double>();
			List<String> future = new ArrayList<String>();
			List<String> future2 = new ArrayList<String>();
			Query quer = em.createQuery("SELECT term FROM Term term WHERE term.season =:season"
					+ " AND term.year =:year");
			String season;
			if (endDateTime.getMonth() == 0) {
				season = "Winter";
			}
			else if (endDateTime.getMonth() > 0 && endDateTime.getMonth() < 5) {
				season = "Spring";
			}
			else if (endDateTime.getMonth() >= 5 && endDateTime.getMonth() < 8) {
				season = "Summer";
			}
			else {
				season = "Fall";
			}
			quer.setParameter("season", season);
			quer.setParameter("year", endDateTime.getYear() + 1900);
			List<Term> result = quer.getResultList();
			Term term = result.get(0);
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
				Query query3 = em.createQuery("SELECT exam FROM Exam exam WHERE exam.startDateTime < :t1 AND exam.endDateTime > :t2 AND (exam.status = :s1 OR exam.status = :s2 OR exam.status = :s3)");
				query3.setParameter("t1", Timestamp.valueOf(date.plusDays(1).atStartOfDay()));
				query3.setParameter("t2", Timestamp.valueOf(date.atStartOfDay()));
				query3.setParameter("s1", Status.APPROVED);
				query3.setParameter("s2", Status.ONGOING);
				query3.setParameter("s3", Status.PENDING);
				List<Exam> exams = query2.getResultList();
				List<Exam> exams2 = query3.getResultList();
				Exam exam = new Exam();
				exam.setDuration(duration);
				exam.setStartDateTime(startDateTime);
				exam.setEndDateTime(endDateTime);
				if (request.getParameter("exam_type").equals("course")) {
					exam.setAdHoc(false);
					Course course = em.find(Course.class, courseId);
					exam.setCourse(course);
				}
				else {
					exam.setAdHoc(true);
					User u;
					String[] netIdsList = netIds.split("\n");
					for (String netId : netIdsList) {
						u = em.find(User.class, netId.trim());
						if (u == null) {
							throw new Exception("User " + netId + " does not exist.");
						}
						if (u.getStudent() == null) {
							throw new Exception("User " + netId + " is not a student.");
						}
						exam.addStudent(u.getStudent());
					}
				}
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
			session.setAttribute("instructorreport", "The current expected utilization from " + startD + " to " + endD + ":<br></br>");
		} catch (Exception e) {
			session.setAttribute("message", e);
		} finally {
			em.close();
			session.setAttribute("requestexam", "yes");
			response.sendRedirect("InstructorUtilization.jsp");
		}
	}

}
