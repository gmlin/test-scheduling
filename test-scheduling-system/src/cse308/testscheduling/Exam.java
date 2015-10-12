package cse308.testscheduling;

import cse308.testscheduling.Course;
import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Exam
 *
 */
@Entity

public class Exam implements Serializable {

	   
	@Id
	private int examID;
	private Course course;
	private boolean adHoc;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startDateTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endDateTime;
	private static final long serialVersionUID = 1L;

	public Exam() {
		super();
	}   
	public int getExamID() {
		return this.examID;
	}

	public void setExamID(int examID) {
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
   
}
