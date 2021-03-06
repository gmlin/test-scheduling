package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	public List<Appointment> getFutureAppointments() {
		EntityManager em = DatabaseManager.createEntityManager();
		Query query = em.createQuery("SELECT appt FROM Appointment appt "
				+ "WHERE appt.dateTime > CURRENT_TIMESTAMP");
		List<Appointment> futureAppointments = null;
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



	public void setUser(User user) {
		this.user = user;
	}

	public Appointment makeAppointment(EntityManager em, String studentId, String examId, Timestamp dateTime, boolean setAside) throws Exception {
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

	public boolean cancelAppointment(EntityManager em, int apptId) throws Exception {
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

	public boolean modifyAppointment(EntityManager em, int apptId, Timestamp dateTime, int seatNumber, boolean attendance) throws Exception {
		try {
			Appointment appt = em.find(Appointment.class, apptId);
			Seat seat = em.find(Seat.class, seatNumber);
			LocalDateTime apptEnd = dateTime.toLocalDateTime().plusMinutes(appt.getExam().getDuration());
			if (dateTime.before(appt.getExam().getStartDateTime())
					|| apptEnd.isAfter(appt.getExam().getEndDateTime().toLocalDateTime())) {
				return false;
			}
			Student student = appt.getStudent();
			for (Appointment app : student.getAppointments()) {
				if (app.overlapsWith(dateTime, app.getExam().getDuration()) && app.getId() != appt.getId()) {
					throw new Exception("Student already has appointment during this time.");
				}
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

	public int checkIn(EntityManager em, String studentId) throws Exception {
		try {
			User user = em.find(User.class, studentId);
			if (user == null) {
				throw new Exception ("User does not exist.");
			}
			Student student = user.getStudent();
			if (student == null) {
				throw new Exception ("User is not a student.");
			}
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




	@SuppressWarnings("unchecked")
	public void modifyTestingCenter(EntityManager em, int numSeats, int numSetAside, Timestamp openTime,
			Timestamp closeTime, int gapTime, int reminderInterval) {
		try{
			Query query = em.createQuery("SELECT term FROM Term term WHERE term.current = true");
			Term term = (Term) query.getResultList().get(0);
			TestingCenter tc = term.getTestingCenter();
			query = em.createQuery("SELECT s FROM Seat s WHERE s.setAside = false", Seat.class);
			List<Seat> seats = query.getResultList();
			query = em.createQuery("SELECT s FROM Seat s WHERE s.setAside = true", Seat.class);
			List<Seat> setAsideSeats = query.getResultList();
			em.getTransaction().begin();
			if (numSeats > seats.size()) {
				for (int i = 0; i < numSeats - seats.size(); i++) {
					Seat seat = new Seat();
					seat.setSetAside(false);
					em.persist(seat);
				}
			}
			if (numSetAside > setAsideSeats.size()) {
				for (int i = 0; i < numSetAside - setAsideSeats.size(); i++) {
					Seat seat = new Seat();
					seat.setSetAside(true);
					em.persist(seat);
				}
			}
			tc.setNumSeats(numSeats);
			tc.setNumSetAsideSeats(numSetAside);
			tc.setOpenTime(openTime);
			tc.setCloseTime(closeTime);
			tc.setGapTime(gapTime);
			tc.setReminderInterval(reminderInterval);
			em.getTransaction().commit();
		}
		catch(Exception e){
			throw e;
		}
		finally{

		}

	}
	
	public void setCurrentTerm(EntityManager em, int termId) {
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("SELECT term FROM Term term WHERE term.current = true");
			Term term = (Term) query.getResultList().get(0);
			Term currentTerm = em.find(Term.class, termId);
			term.setCurrent(false);
			currentTerm.setCurrent(true);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public int seatsAvailable(String dateTime) {
		try {
			EntityManager em = DatabaseManager.createEntityManager();
			Query query = em.createQuery("SELECT term FROM Term term WHERE term.current = true");
			Term term = (Term) query.getResultList().get(0);
			TestingCenter tc = term.getTestingCenter();
			em.close();
			return tc.getNumSeats() + tc.getNumSetAsideSeats() - getAppointments(dateTime).size();
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public List<Appointment> getAppointments(String dateTime) {
		List<Appointment> appts = new ArrayList<Appointment>();
		for (Appointment appt : getFutureAppointments()) {
			if (appt.overlapsWith(Timestamp.valueOf(dateTime), 0)) {
				appts.add(appt);
			}
		}
		return appts;
	}
}
