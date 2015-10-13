package cse308.testscheduling;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Appointment
 *
 */
@Entity

public class Appointment implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	//primary key is appointmentId
	private int appointmentId;
	private boolean setAsideSeat;

	//a seat can belongs to multiple appointments
	//the "SEAT_NUMBER" is the name of the column 
	//in the table corresponding to this class
	//that identifies the one associated instance of seat.
	//That is the column corresponding to appointment
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEAT_NUMBER")
	private Seat seat;

	//a student can have multiple appointments.
	//"STUDENT_ID" is the column name corresponding to student
	//in the appointment table
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STUDENT_ID")
	private Student student;

	//a exam can have multiple appointments.
	//"EXAM_ID" is the column name corresponding to student
	//in the appointment table
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EXAM_ID")
	private Exam exam;
	
	//temporal must be  specified for persistent fields or 
	//properties of type java.util.Date and java.util.Calendar
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dateTime;
	
	private static final long serialVersionUID = 1L;

	public Appointment() {
		super();
	}   
	public Seat getSeat() {
		return this.seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}   
	public boolean getSetAsideSeat() {
		return this.setAsideSeat;
	}
	public void setSetAsideSeat(boolean setAside) {
		this.setAsideSeat = setAside;
	}
	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}   
	public Exam getExam() {
		return this.exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}
	public Calendar getDateTime() {
		return dateTime;
	}
	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}   
	
}
