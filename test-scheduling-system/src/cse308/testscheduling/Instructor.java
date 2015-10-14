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

	//primary key is Id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	
	//this is a one-to-one association between instructor and user, 
	//instructor field specifies the role of user
	@OneToOne(optional=false)
	@JoinColumn(name="NET_ID")
	private User user;
	
	//a course has multiple instructors, instructor can teach multiple courses
	@ManyToMany(mappedBy="instructors")
	private List<Course> courses;
	
	/*
	//a instructor can associate with multiple exams
	@OneToMany(mappedBy="instructor")
	private List<Exam> exams;
	*/
	
	private static final long serialVersionUID = 1L;

	public Instructor() {
		super();
		setCourses(new ArrayList<Course>());
	}   
	
	public void addCourse(Course course) {
		getCourses().add(course);
	}
	/*public List<Request> getRequests() {
		return requests;
	}
	public void setRequests(List<Request> requests) {
		this.requests = requests;
	} */
	/*public List<Exam> getExams() {
		return exams;
	}
	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}*/
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
   
}
