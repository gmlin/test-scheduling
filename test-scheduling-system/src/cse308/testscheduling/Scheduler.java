package cse308.testscheduling;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Scheduler
 *
 */
@Entity

public class Scheduler implements Serializable {

	@Id
	private int id;
	private List appointments;
	private List exams;
	private List requests;
	private static final long serialVersionUID = 1L;

	public Scheduler() {
		super();
	}   
	public List getAppointments() {
		return this.appointments;
	}

	public void setAppointments(List appointments) {
		this.appointments = appointments;
	}   
	public List getExams() {
		return this.exams;
	}

	public void setExams(List exams) {
		this.exams = exams;
	}   
	public List getRequests() {
		return this.requests;
	}

	public void setRequests(List requests) {
		this.requests = requests;
	}
   
}
