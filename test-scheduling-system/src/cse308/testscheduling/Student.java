package cse308.testscheduling;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;

import cse308.testscheduling.servlet.DatabaseManager;

/**
 * Entity implementation class for Entity: Student
 *
 */
@Entity

public class Student implements Serializable {

	private static final long serialVersionUID = 1L;

	// primary key is studentID
	@Id
	@Column(name = "STUDENT_ID")
	private int studentId;

	// this is a one-to-one association between student and user,
	// student field specifies the role of user
	@OneToOne(optional = false)
	@JoinColumn(name = "NET_ID")
	private User user;

	// a student can have multiple appointments
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<Appointment> appointments;

	// a student can take many courses
	// a course can have many students
	@ManyToMany(mappedBy = "students")
	private List<Course> courses;

	// a many-to-many association between students,
	// and their available ad hoc exams
	@ManyToMany(mappedBy = "students", cascade = CascadeType.PERSIST)
	private List<Exam> adHocExams;

	public Student() {
		super();
		appointments = new ArrayList<Appointment>();
		setCourses(new ArrayList<Course>());
		adHocExams = new ArrayList<Exam>();
	}

	public void addAdHocExam(Exam adHocExam) {
		adHocExams.add(adHocExam);
	}

	public void addAppointment(Appointment appointment) {
		appointments.add(appointment);
	}

	public void addCourse(Course course) {
		getCourses().add(course);
	}

	public List<Exam> getAdHocExams() {
		return adHocExams;
	}

	public List<Appointment> getAppointments() {
		return this.appointments;
	}
	
	public List<Appointment> getSortedAppointments() {
		EntityManager em = DatabaseManager.createEntityManager();
		Query query = em.createQuery("SELECT appt FROM Appointment appt "
				+ "WHERE appt.student = :student "
				+ "ORDER BY appt.id DESC", Appointment.class);
		query.setParameter("student", this);
		List<Appointment> sortedAppointments = null;
		try {
			sortedAppointments = query.getResultList();
		} catch (Exception e) {
		} finally {
			em.close();
		}
		return sortedAppointments;
	}

	public List<Exam> getAvailableExams() {
		List<Exam> availableExams = new ArrayList<Exam>();
		List<Exam> alreadyAppointed = new ArrayList<Exam>();
		for (Appointment a : appointments) {
			alreadyAppointed.add(a.getExam());
		}
		for (Course c : courses) {
			List<Exam> exams = c.getExams();
			for (Exam e : exams) {
				if (e.getStatus() == Status.APPROVED && !alreadyAppointed.contains(e))
					availableExams.add(e);
			}
		}
		for (Exam e : adHocExams) {
			if (e.getStatus() == Status.APPROVED && !alreadyAppointed.contains(e))
				availableExams.add(e);
		}
		return availableExams;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public int getStudentId() {
		return studentId;
	}

	public User getUser() {
		return user;
	}

	@SuppressWarnings("unchecked")
	public Appointment makeAppointment(String examId, Timestamp dateTime, boolean setAside) {
		EntityManager em = DatabaseManager.createEntityManager();
		try {
			em.getTransaction().begin();
			Query query;
			if (setAside) {
				query = em.createQuery("SELECT s FROM Seat s WHERE s.setAside = true", Seat.class);
			}
			else {
				query = em.createQuery("SELECT s FROM Seat s WHERE s.setAside = false", Seat.class);
			}
			List<Seat> seats = query.getResultList();
			Exam exam = em.find(Exam.class, examId);
			Appointment appt = null;
			for (Seat seat : seats) {
				if (seat.isAppointable(dateTime, exam)) {
					appt = new Appointment();
					appt.setAttendance(false);
					appt.setDateTime(dateTime);
					appt.setExam(exam);
					appt.setSeat(seat);
					appt.setSetAsideSeat(setAside);
					appt.setExam(exam);
					this.addAppointment(appt);
					exam.addAppointment(appt);
					appt.setStudent(this);
					seat.addAppointment(appt);
					em.persist(appt);
					break;
				}
			}
			if (appt != null) {
				em.getTransaction().commit();
			}
			return appt;
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
	}
	
	public void setAdHocExams(List<Exam> adHocExams) {
		this.adHocExams = adHocExams;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
