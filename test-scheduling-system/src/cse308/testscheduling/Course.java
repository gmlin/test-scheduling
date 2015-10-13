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

	//The primary key in nameed "COURSE_ID" in course table   
	@Id
	@Column(name="COURSE_ID")
	private String courseID;
	
	//a course has multiple students, student can have multiple courses
	//generate new table contains the course id column and student net id column
	@ManyToMany
	@JoinTable(
		name="COURSE_STUDENT",
		joinColumns={@JoinColumn(name="COURSE_ID", referencedColumnName="COURSE_ID")},
		inverseJoinColumns={@JoinColumn(name="STUDENT_NET_ID", referencedColumnName="NET_ID")})
	private List<Student> roster;
	
	//a course has multiple instructors, instructor can teach multiple courses
	//generate new table contains the course id column and instructor net id column
	@ManyToMany
	@JoinTable(
		name="COURSE_INSTRUCTOR",
		joinColumns={@JoinColumn(name="COURSE_ID", referencedColumnName="course_ID")},
		inverseJoinColumns={@JoinColumn(name="INSTRUCTOR_NET_ID", referencedColumnName="NET_ID")})
	private List<Instructor> instructors;
	
	//a course can have multiple exam, so it is one-to-many
	// the mappedBy element indicates that this is the non−owning side of
	// the association.
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
	public List<Instructor> getInstructors() {
		return instructors;
	}
	public void setInstructors(List<Instructor> instructors) {
		this.instructors = instructors;
	}
   
}
