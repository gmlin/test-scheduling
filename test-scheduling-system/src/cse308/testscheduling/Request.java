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

	   
	@Id
	private int requestID;
	private Instructor instructor;
	private int status;
	private static final long serialVersionUID = 1L;

	public Request() {
		super();
	}   
	public int getRequestID() {
		return this.requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}   
	public Instructor getInstructor() {
		return this.instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}   
	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
   
}