package cse308.testscheduling;

import cse308.testscheduling.Appointment;
import cse308.testscheduling.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Student
 *
 */
@Entity

public class Student implements Serializable {

	//primary key is studentID
	@Id
	@Column(name="STUDENT_ID")
	private int studentId;
	
	//this is a one-to-one association between student and user, 
	//student field specifies the role of user
	@OneToOne(optional=false)
	@JoinColumn(name="NET_ID")
	private User user;
	
	//a student can have multiple appointments
	@OneToMany(mappedBy="student")
	private List<Appointment> appointments;
	
	//a student can take many courses
	//a course can have many students
	@ManyToMany(mappedBy="students")
	private List<Course> courses;
	private static final long serialVersionUID = 1L;

	public Student() {
		super();
		appointments = new ArrayList<Appointment>();
		courses = new ArrayList<Course>();
	}   
	public List<Appointment> getAppointments() {
		return this.appointments;
	}

	public void addAppointment(Appointment appointment) {
		appointments.add(appointment);
	}
	
	public void addCourse(Course course) {
		courses.add(course);
	}
	
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
   
}
