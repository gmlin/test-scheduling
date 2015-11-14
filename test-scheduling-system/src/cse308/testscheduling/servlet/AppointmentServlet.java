package cse308.testscheduling.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cse308.testscheduling.Appointment;
import cse308.testscheduling.DatabaseManager;
import cse308.testscheduling.Exam;
import cse308.testscheduling.Seat;
import cse308.testscheduling.Student;
import cse308.testscheduling.User;

/**
 * Servlet implementation class AppointmentServlet
 */
@WebServlet("/make_appointment")
public class AppointmentServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppointmentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession s = request.getSession();
		
		Student student = null;
		String examId = request.getParameter("exam");
		Timestamp dateTime = Timestamp.valueOf(request.getParameter("dateTime").replace("T", " ") + ":00");
		EntityManager em = DatabaseManager.createEntityManager();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("SELECT s FROM Seat s",
					User.class);
			List<Seat> seats = query.getResultList();
			Exam exam = em.find(Exam.class, examId);
			System.out.println(exam.getExamId());
			Appointment appt = null;
			for (Seat seat : seats) {
				if (seat.isAppointable(dateTime, exam)) {
					appt = new Appointment();
					appt.setAttendance(false);
					appt.setDateTime(dateTime);
					appt.setExam(exam);
					appt.setSeat(seat);
					if (request.getParameter("appt_type").equals("student")) {
						student = ((User)(s.getAttribute("user"))).getStudent();
						appt.setSetAsideSeat(false);
						appt.setExam(exam);
						student.addAppointment(appt);
						exam.addAppointment(appt);
					}
					else if (request.getParameter("appt_type").equals("admin")) {
						if (request.getAttribute("student") == null) {
							appt.setSetAsideSeat(true);
							appt.setExam(null);
						}
						else {
							student = em.find(Student.class, request.getAttribute("student"));
							if (student == null)
								throw new Exception("Student not found.");
							if (student.getAvailableExams().contains(exam))
								throw new Exception("Student cannot take this exam");
							student.addAppointment(appt);
							appt.setStudent(student);
							exam.addAppointment(appt);
							appt.setExam(exam);
							appt.setSetAsideSeat(false);
						}
					}
					appt.setSetAsideSeat(false);
					appt.setStudent(student);
					seat.addAppointment(appt);
					request.getSession().setAttribute("message", "Successfully scheduled appointment: Seat " +
							seat.getSeatNumber());
					em.persist(appt);
					break;
				}
			}
			if (appt == null) {
				request.getSession().setAttribute("message", "Failed to schedule appointment appointment.");
			}
			else {
				em.getTransaction().commit();
			}
			
		}
		catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			request.getSession().setAttribute("message", exceptionAsString);
		}
		finally {
			response.sendRedirect(request.getHeader("Referer"));
			em.close();
			//logger.exiting(getClass().getName(), "doPost");
		}
	}

}
