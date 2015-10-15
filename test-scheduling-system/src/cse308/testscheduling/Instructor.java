package cse308.testscheduling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Entity implementation class for Entity: Instructor
 *
 */
@Entity

public class Instructor implements Serializable {

	private static final long serialVersionUID = 1L;

	// primary key is Id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// this is a one-to-one association between instructor and user,
	// instructor field specifies the role of user
	@OneToOne(optional = false)
	@JoinColumn(name = "NET_ID")
	private User user;

	/*
	 * //a instructor can associate with multiple exams
	 * 
	 * @OneToMany(mappedBy="instructor") private List<Exam> exams;
	 */

	// a course has multiple instructors, instructor can teach multiple courses
	@ManyToMany(mappedBy = "instructors")
	private List<Course> courses;

	// a instructor can have multiple ad hoc exams
	@OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
	private List<Exam> adHocExams;
	
	public Instructor() {
		super();
		setCourses(new ArrayList<Course>());
		adHocExams = new ArrayList<Exam>();
	}

	public void addCourse(Course course) {
		getCourses().add(course);
	}
	/*
	 * public List<Request> getRequests() { return requests; } public void
	 * setRequests(List<Request> requests) { this.requests = requests; }
	 */
	/*
	 * public List<Exam> getExams() { return exams; } public void
	 * setExams(List<Exam> exams) { this.exams = exams; }
	 */

	public List<Course> getCourses() {
		return courses;
	}

	public User getUser() {
		return user;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
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
	
}
