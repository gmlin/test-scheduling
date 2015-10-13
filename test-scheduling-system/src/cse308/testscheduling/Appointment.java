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
	private int appointmentID;
	private boolean setAsideSeat;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEAT_NUMBER")
	private Seat seat;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STUDENT_ID")
	private int studentID;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EXAM_ID")
	private int examID;
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
	public int getStudentID() {
		return this.studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}   
	public int getExamID() {
		return this.examID;
	}

	public void setExamID(int examID) {
		this.examID = examID;
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
