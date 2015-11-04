package cse308.testscheduling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
	@OneToMany(mappedBy = "student")
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

	public void addAppointment(Appointment appointment) {
		appointments.add(appointment);
	}

	public void addCourse(Course course) {
		getCourses().add(course);
	}

	public List<Appointment> getAppointments() {
		return this.appointments;
	}

	public int getStudentId() {
		return studentId;
	}

	public User getUser() {
		return user;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Exam> getAdHocExams() {
		return adHocExams;
	}

	public void setAdHocExams(List<Exam> adHocExams) {
		this.adHocExams = adHocExams;
	}
	
	public void addAdHocExam(Exam adHocExam) {
		adHocExams.add(adHocExam);
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
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
	
}
