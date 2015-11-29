package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;

import cse308.testscheduling.servlet.DatabaseManager;

/**
 * Entity implementation class for Entity: Exam
 *
 */
@Entity

public class Exam implements Serializable, Comparable<Exam> {

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
	@OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
	private List<Appointment> appointments;

	// for adhoc exam only
	@ManyToMany
	@JoinTable(name = "AD_HOC_EXAM_STUDENT", joinColumns = {
			@JoinColumn(name = "EXAM_ID", referencedColumnName = "EXAM_ID") }, inverseJoinColumns = {
					@JoinColumn(name = "NET_ID", referencedColumnName = "NET_ID") })
	private List<Student> students;

	// for adhoc exam only
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

	public void addStudent(Student student) {
		students.add(student);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exam other = (Exam) obj;
		if (examId == null) {
			if (other.examId != null)
				return false;
		} else if (!examId.equals(other.examId))
			return false;
		return true;
	}

	public boolean getAdHoc() {
		return this.adHoc;
	}

	public int getAttendance() {
		int attended = 0;
		for (Appointment appt : appointments) {
			if (appt.getAttendance())
				attended++;
		}
		return attended;
	}
	
	public List<Appointment> getAppointments() {
		return appointments;
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

	public Instructor getInstructor() {
		return instructor;
	}

	public Timestamp getStartDateTime() {
		return startDateTime;
	}

	public Status getStatus() {
		return status;
	}

	public List<Student> getStudents() {
		if (adHoc) {
			return students;
		}
		else {
			return course.getStudents();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((examId == null) ? 0 : examId.hashCode());
		return result;
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

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public void setStartDateTime(Timestamp startDateTime) {
		this.startDateTime = startDateTime;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	@Override
	public int compareTo(Exam o) {
		return o.getStartDateTime().compareTo(this.getStartDateTime());
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public Term getTerm() {
		if (adHoc) {
			try {
				EntityManager em = DatabaseManager.createEntityManager();
				Query query = em.createQuery("SELECT term FROM Term term WHERE term.season =:season"
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
				query.setParameter("season", season);
				query.setParameter("year", endDateTime.getYear() + 1900);
				List<Term> result = query.getResultList();
				if (result.size() == 0) {
					return null;
				}
				else {
					return result.get(0);
				}
			}
			catch (Exception e) {
				throw e;
			}
		}
		else {
			return course.getTerm();
		}
	}
}
