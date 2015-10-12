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
	private List<Appointment> appointments;
	private List<Exam> exams;
	private List<Request> requests;
	private static final long serialVersionUID = 1L;

	public Scheduler() {
		super();
	}   
	public List<Appointment> getAppointments() {
		return this.appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}   
	public List<Exam> getExams() {
		return this.exams;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}   
	public List<Request> getRequests() {
		return this.requests;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}
   
}
