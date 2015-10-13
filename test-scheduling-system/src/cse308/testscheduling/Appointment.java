package cse308.testscheduling;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Appointment
 *
 */
@Entity

public class Appointment implements Serializable {

	   
	@Id
	//primary key is appointmentID
	private int appointmentID;
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
	private Date date;
	private Time time;
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
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}   
	public Time getTime() {
		return this.time;
	}

	public void setTime(Time time) {
		this.time = time;
	}
   
}
