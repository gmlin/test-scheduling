package cse308.testscheduling;

import java.io.Serializable;
import java.lang.String;
import java.util.List;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Course
 *
 */
@Entity

public class Course implements Serializable {

	   
	@Id
	@Column(name="COURSE_ID")
	private String courseID;
	@ManyToMany
	@JoinTable(
		name="COURSE_STUDENT",
		joinColumns={@JoinColumn(name="COURSE_ID", referencedColumnName="COURSE_ID")},
		inverseJoinColumns={@JoinColumn(name="STUDENT_NET_ID", referencedColumnName="NET_ID")})
	private List<Student> roster;
	@ManyToMany
	@JoinTable(
		name="COURSE_INSTRUCTOR",
		joinColumns={@JoinColumn(name="COURSE_ID", referencedColumnName="courseID")},
		inverseJoinColumns={@JoinColumn(name="INSTRUCTOR_NET_ID", referencedColumnName="NET_ID")})
	private List<Instructor> instructors;
	@OneToMany(mappedBy="course")
	private List<Exam> exams;
	private static final long serialVersionUID = 1L;

	public Course() {
		super();
	}   
	public String getCourseID() {
		return this.courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}   
	public List<Student> getRoster() {
		return this.roster;
	}

	public void setRoster(List<Student> roster) {
		this.roster = roster;
	}
   
}
