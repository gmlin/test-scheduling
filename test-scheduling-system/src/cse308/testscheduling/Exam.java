package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import cse308.testscheduling.Instructor;

/**
 * Entity implementation class for Entity: Exam
 *
 */
@Entity

public class Exam implements Serializable {

	private static final long serialVersionUID = 1L;

	// primary key is examId
	@Id
	@Column(name = "EXAM_ID")
	private String examId;
	//// a course can have multiple exams.
	// "COURSE_ID" is the column name corresponding to course
	// in the exam table
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID")
	private Course course;
	@Column(name = "AD_HOC")
	private boolean adHoc;
	private int duration;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "START_DATE_TIME")
	private Timestamp startDateTime;

	@Column(name = "END_DATE_TIME")
	private Timestamp endDateTime;

	// a exam can have multiple appointments, so it is one-to-many
	// the mappedBy element indicates that this is the non owning side of
	// the association.
	@OneToMany(mappedBy = "exam")
	private List<Appointment> appointments;
	
	//for adhoc exam only
	@ManyToMany
	@JoinTable(name = "AD_HOC_EXAM_STUDENT", joinColumns = {
			@JoinColumn(name = "EXAM_ID", referencedColumnName = "EXAM_ID") }, inverseJoinColumns = {
					@JoinColumn(name = "NET_ID", referencedColumnName = "NET_ID") })
	private List<Student> students;

	//for adhoc exam only
		@ManyToOne
		private Instructor instructor;
	
	public Exam() {
		super();
		appointments = new ArrayList<Appointment>();
		students = new ArrayList<Student>();
	}

	public void addAppointment(Appointment appointment) {
		appointments.add(appointment);
	}

	public boolean getAdHoc() {
		return this.adHoc;
	}

	public Course getCourse() {
		return this.course;
	}

	public int getDuration() {
		return duration;
	}

	public Timestamp getEndDateTime() {
		return endDateTime;
	}

	public String getExamId() {
		return this.examId;
	}

	public Timestamp getStartDateTime() {
		return startDateTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setAdHoc(boolean adHoc) {
		this.adHoc = adHoc;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setEndDateTime(Timestamp endDateTime) {
		this.endDateTime = endDateTime;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public void setStartDateTime(Timestamp startDateTime) {
		this.startDateTime = startDateTime;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}
	
	public void addStudent(Student student) {
		students.add(student);
	}
	
	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
	
}
