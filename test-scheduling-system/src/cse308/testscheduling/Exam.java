package cse308.testscheduling;

import cse308.testscheduling.Course;
import java.io.Serializable;
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
   
}
