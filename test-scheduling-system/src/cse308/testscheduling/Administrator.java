package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.OneToOne;
import javax.persistence.Query;

import cse308.testscheduling.servlet.DatabaseManager;

/**
 * Entity implementation class for Entity: Administrator
 */
@Entity

public class Administrator implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// id is the primary key in admin table
	private int id;

	// this is a one-to-one association between admin and user,
	// admin field specifies the role of user
	@OneToOne(optional = false)
	@JoinColumn(name = "NET_ID")
	private User user;

	// this is a many-to-one association between admin and testing center
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TESTINGCENTER_ID")
	private TestingCenter testingCenter;

	public Administrator() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public List<Exam> getApprovedExams() {
		EntityManager em = DatabaseManager.createEntityManager();
		Query query = em.createQuery("SELECT exam FROM Exam exam "
				+ "WHERE exam.status = :status", Exam.class);
		query.setParameter("status", Status.APPROVED);
		List<Exam> approvedExams = null;
		try {
			approvedExams = query.getResultList();
		} catch (NoResultException e) {
		} finally {
			em.close();
		}
		return approvedExams ;
	}

	@SuppressWarnings("unchecked")
	public List<Exam> getFutureAppointments() {
		EntityManager em = DatabaseManager.createEntityManager();
		Query query = em.createQuery("SELECT appt FROM Appointment appt "
				+ "WHERE appt.dateTime > CURRENT_TIMESTAMP");
		List<Exam> futureAppointments = null;
		try {
			futureAppointments = query.getResultList();
		} catch (Exception e) {
			throw e;
		}
		finally {
			em.close();
		}
		return futureAppointments;
	}
	
	@SuppressWarnings("unchecked")
	public List<Exam> getPendingExams() {
		EntityManager em = DatabaseManager.createEntityManager();
		Query query = em.createQuery("SELECT exam FROM Exam exam "
				+ "WHERE exam.status = :status", Exam.class);
		query.setParameter("status", Status.PENDING);
		List<Exam> pendingExams = null;
		try {
			pendingExams = query.getResultList();
		} catch (Exception e) {
			 throw e;
		} finally {
			em.close();
		}
		return pendingExams;
	}

	public TestingCenter getTestingCenter() {
		return testingCenter;
	}

	public User getUser() {
		return user;
	}

	public void modifyRequest(EntityManager em, String examId, Status status) {
		//EntityManager em = DatabaseManager.createEntityManager();
		try {
			em.getTransaction().begin();
			Exam exam = em.find(Exam.class, examId);
			exam.setStatus(status);
			em.getTransaction().commit();
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			//em.close();
		}
	}
	
	public void setTestingCenter(TestingCenter testingCenter) {
		this.testingCenter = testingCenter;
	}
	
	public void modifyTestingCenter(EntityManager em, TestingCenter testingCenter){
		try{
			em.getTransaction().begin();
			TestingCenter tc = em.find(TestingCenter.class, testingCenter.getId());
			//tc.setNumSeats(testingCenter.getNumSeats());
			tc = testingCenter;
			em.persist(tc);
			em.getTransaction().commit();
		}
		catch(Exception e){
			throw e;
		}
		finally{
			
		}
		
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Appointment makeAppointment(EntityManager em, String studentId, String examId, Timestamp dateTime, boolean setAside) {
		//EntityManager em = DatabaseManager.createEntityManager();
		try {
			User user = em.find(User.class, studentId);
			Student student = user.getStudent();
			return student.makeAppointment(em, examId, dateTime, setAside);
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			//em.close();
		}
	}

	public boolean cancelAppointment(EntityManager em, int apptId) {
		try {
			Appointment appt = em.find(Appointment.class, apptId);
			return appt.getStudent().cancelAppointment(em, apptId, true);
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			
		}
	}

	public boolean modifyAppointment(EntityManager em, int apptId, Timestamp dateTime, int seatNumber, boolean attendance) {
		try {
			Appointment appt = em.find(Appointment.class, apptId);
			Seat seat = em.find(Seat.class, seatNumber);
			LocalDateTime apptEnd = dateTime.toLocalDateTime().plusMinutes(appt.getExam().getDuration());
			if (dateTime.before(appt.getExam().getStartDateTime())
				|| apptEnd.isAfter(appt.getExam().getEndDateTime().toLocalDateTime())) {
				return false;
			}
			if ((appt.getSeat().equals(seat) || seat.examAt(dateTime) == null)) {
				em.getTransaction().begin();
				appt.getSeat().getAppointments().remove(appt);
				appt.setSeat(seat);
				appt.setDateTime(dateTime);
				appt.setAttendance(attendance);
				seat.addAppointment(appt);
				em.getTransaction().commit();
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			
		}
	}

	public int checkIn(EntityManager em, String studentId) {
		try {
			User user = em.find(User.class, studentId);
			Student student = user.getStudent();
			for (Appointment appt : student.getAppointments()) {
				if (appt.getDateTime().toLocalDateTime().isBefore(LocalDateTime.now())
						&& appt.getEndDateTime().toLocalDateTime().isAfter(LocalDateTime.now())) {
					appt.setAttendance(true);
					return appt.getSeat().getSeatNumber();
				}
			}
			return -1;
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			
		}
	}

}
