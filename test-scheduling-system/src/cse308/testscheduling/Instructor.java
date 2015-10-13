package cse308.testscheduling;

import cse308.testscheduling.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Instructor
 *
 */
@Entity

public class Instructor implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int ID;
	@OneToOne(optional=false)
	private User user;
	@ManyToMany(mappedBy="instructors")
	private List<Course> courses;
	//private List<Request> requests; don't need?
	@OneToMany(mappedBy="instructor")
	private List<Exam> exams;
	private int numCourses;
	private static final long serialVersionUID = 1L;

	public Instructor() {
		super();
	}   
	public List<Course> getCourses() {
		return this.courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	/*public List<Request> getRequests() {
		return requests;
	}
	public void setRequests(List<Request> requests) {
		this.requests = requests;
	} */
	public List<Exam> getExams() {
		return exams;
	}
	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getNumCourses() {
		return numCourses;
	}
	public void setNumCourses(int numCourses) {
		this.numCourses = numCourses;
	}
   
}
