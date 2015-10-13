package cse308.testscheduling;

import cse308.testscheduling.Appointment;
import cse308.testscheduling.User;
import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Student
 *
 */
@Entity

public class Student implements Serializable {

	@Id
	private int StudentID;
	@OneToOne(mappedBy="student",optional=false)
	private User user;
	@OneToMany(mappedBy="studentID")
	private List<Appointment> appointments;
	@ManyToMany(mappedBy="roster")
	private List<Course> courses;
	private static final long serialVersionUID = 1L;

	public Student() {
		super();
	}   
	public List<Appointment> getAppointments() {
		return this.appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}   
	public int getStudentID() {
		return this.StudentID;
	}

	public void setStudentID(int StudentID) {
		this.StudentID = StudentID;
	}
	public List<Course> getCourses() {
		return courses;
	}
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
   
}
