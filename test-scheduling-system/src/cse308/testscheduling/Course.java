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
	private String courseID;
	private List<Student> roster;
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
