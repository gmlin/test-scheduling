package cse308.testscheduling;

import cse308.testscheduling.Course;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Exam
 *
 */
@Entity

public class Exam implements Serializable {

	//primary key is examID   
	@Id
	private String examID;
	
	////a course can have multiple exams.
	//"COURSE_ID" is the column name corresponding to course
	//in the exam table
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COURSE_ID")
	private Course course;
	private boolean adHoc;
	
	//temporal must be  specified for persistent fields or 
	//properties of type java.util.Date and java.util.Calendar
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startDateTime;
	
	//temporal must be  specified for persistent fields or 
	//properties of type java.util.Date and java.util.Calendar
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endDateTime;
	
	//a exam can have multiple appointments, so it is one-to-many
	// the mappedBy element indicates that this is the non−owning side of
	// the association.
	@OneToMany(mappedBy="exam")
	private List<Appointment> appointments;
	
	//each request is for one exam. so one-to-one
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REQUEST_ID")
	private Request request;
	
	//a instructor can associate with multiple exams
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INSTRUCTOR_ID")
	private Instructor instructor;
	private static final long serialVersionUID = 1L;

	public Exam() {
		super();
	}   
	public String getExamID() {
		return this.examID;
	}

	public void setExamID(String examID) {
		this.examID = examID;
	}   
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}   
	public boolean getAdHoc() {
		return this.adHoc;
	}

	public void setAdHoc(boolean adHoc) {
		this.adHoc = adHoc;
	}
	public Calendar getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Calendar startDateTime) {
		this.startDateTime = startDateTime;
	}
	public Calendar getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(Calendar endDateTime) {
		this.endDateTime = endDateTime;
	}
    public Instructor getInstructor() {
    	return instructor;
    }
}
