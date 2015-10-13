package cse308.testscheduling;

import cse308.testscheduling.Instructor;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Request
 *
 */
@Entity

public class Request implements Serializable {

	//primary key is requestId   
	@Id
	private int requestId;
	
	//a request is associate with one exam in the table
	@OneToOne(mappedBy="request")
	private Exam exam;
	//private Instructor instructor; not needed?
	private int status;
	private static final long serialVersionUID = 1L;

	public Request() {
		super();
	}   
	public int getRequestId() {
		return this.requestId;
	}

	public void setRequestID(int requestId) {
		this.requestId = requestId;
	}   
	/*public Instructor getInstructor() {
		return this.instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}*/
	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public Exam getExam() {
		return exam;
	}
	
	public void setExam(Exam exam) {
		this.exam = exam;
	}
   
}
