package cse308.testscheduling;

import cse308.testscheduling.User;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Instructor
 *
 */
@MappedSuperclass

public class Instructor extends User implements Serializable {

	
	private List courses;
	private static final long serialVersionUID = 1L;

	public Instructor() {
		super();
	}   
	public List getCourses() {
		return this.courses;
	}

	public void setCourses(List courses) {
		this.courses = courses;
	}
   
}
