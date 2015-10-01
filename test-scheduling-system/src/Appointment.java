

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
	private int seatNumber;
	private int studentID;
	private int examID;
	private Date date;
	private Time time;
	private static final long serialVersionUID = 1L;

	public Appointment() {
		super();
	}   
	public int getSeatNumber() {
		return this.seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
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
