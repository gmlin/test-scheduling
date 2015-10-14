package cse308.testscheduling;

import cse308.testscheduling.Course;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Exam
 *
 */
@Entity

public class Exam implements Serializable {

	//primary key is examId   
	@Id
	private String examId;
	
	////a course can have multiple exams.
	//"COURSE_ID" is the column name corresponding to course
	//in the exam table
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COURSE_ID")
	private Course course;
	@Column(name="AD_HOC")
	private boolean adHoc;
	private int duration;
	//0 is waiting for approval
	//1 is approved
	//2 is denied
	//3 is exam ended
	private int status;
	
	//temporal must be  specified for persistent fields or 
	//properties of type java.util.Date and java.util.Calendar
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START_DATE_TIME")
	private Calendar startDateTime;
	
	//temporal must be  specified for persistent fields or 
	//properties of type java.util.Date and java.util.Calendar
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END_DATE_TIME")
	private Calendar endDateTime;
	
	//a exam can have multiple appointments, so it is one-to-many
	// the mappedBy element indicates that this is the non−owning side of
	// the association.
	@OneToMany(mappedBy="exam")
	private List<Appointment> appointments;
	
	private static final long serialVersionUID = 1L;

	public Exam() {
		super();
		appointments = new ArrayList<Appointment>();
	}   
	public String getExamId() {
		return this.examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
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
	
    public void addAppointment(Appointment appointment) {
    	appointments.add(appointment);
    }
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
