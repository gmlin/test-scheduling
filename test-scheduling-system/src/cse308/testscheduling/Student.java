package cse308.testscheduling;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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

	public boolean cancelAppointment(EntityManager em, int apptId, boolean admin) {
		//EntityManager em = DatabaseManager.createEntityManager();
		em.getTransaction().begin();
		try {
			Appointment appt = em.find(Appointment.class, apptId);
			if (appt == null || !appt.getStudent().equals(this))
				return false;
			if (admin || appt.isCancelable()) {
				appt.getExam().getAppointments().remove(appt);
				appt.getSeat().getAppointments().remove(appt);
				this.getAppointments().remove(appt);
				em.remove(appt);
				em.getTransaction().commit();
			}
			else
				return false;
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			//em.close();
		}
		return true;
	}

	public List<Exam> getAdHocExams() {
		return adHocExams;
	}

	public List<Appointment> getAppointments() {
		return this.appointments;
	}

	public List<Appointment> getSortedAppointments() {
		Collections.sort(appointments);
		System.out.println(appointments.size());
		return appointments;
	}

	public List<Exam> getAvailableExams() {
		List<Exam> availableExams = new ArrayList<Exam>();
		List<Exam> appointmentMade = new ArrayList<Exam>();
		for (Appointment appt : appointments) {
			appointmentMade.add(appt.getExam());
		}
		for (Course course : this.getCourses()) {
			for (Exam exam : course.getExams()) {
				if (exam.getStatus() == Status.APPROVED && !appointmentMade.contains(exam)) {
					availableExams.add(exam);
				}
			}
		}
		for (Exam exam : adHocExams) {
			if (exam.getStatus() == Status.APPROVED && !appointmentMade.contains(exam)) {
				availableExams.add(exam);
			}
		}
		Collections.sort(availableExams);
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
	public Appointment makeAppointment(EntityManager em, String examId, Timestamp dateTime, boolean setAside) {
		//EntityManager em = DatabaseManager.createEntityManager();
		try {
			Query query = em.createQuery("SELECT appt FROM Appointment appt "
					+ "WHERE appt.student = :student AND appt.exam =:exam");
			Exam exam = em.find(Exam.class, examId);
			query.setParameter("student", this);
			query.setParameter("exam", exam);
			if (!query.getResultList().isEmpty())
				return null;
			em.getTransaction().begin();
			if (setAside) {
				query = em.createQuery("SELECT s FROM Seat s WHERE s.setAside = true", Seat.class);
			}
			else {
				query = em.createQuery("SELECT s FROM Seat s WHERE s.setAside = false", Seat.class);
			}
			List<Seat> seats = query.getResultList();
			Appointment appt = null;
			for (Seat seat : seats) {
				if (seat.isAppointable(dateTime, exam)) {
					appt = new Appointment();
					appt.setAttendance(false);
					appt.setDateTime(dateTime);
					appt.setExam(exam);
					appt.setSeat(seat);
					appt.setSetAsideSeat(setAside);
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
			//em.close();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + studentId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (studentId != other.studentId)
			return false;
		return true;
	}


}
