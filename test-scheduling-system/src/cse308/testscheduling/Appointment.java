package cse308.testscheduling;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: Appointment
 *
 */
@Entity

public class Appointment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// primary key is id
	private int id;
	
	@Column(name = "SET_ASIDE_SEAT")
	private boolean setAsideSeat;

	// a seat can belong to multiple appointments
	// the "SEAT_NUMBER" is the name of the column
	// in the table corresponding to this class
	// that identifies the one associated instance of seat.
	// That is the column corresponding to appointment
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEAT_NUMBER")
	private Seat seat;

	// a student can have multiple appointments.
	// "STUDENT_ID" is the column name corresponding to student
	// in the appointment table
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	private Student student;

	// a exam can have multiple appointments.
	// "EXAM_ID" is the column name corresponding to student
	// in the appointment table
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAM_ID")
	private Exam exam;

	// temporal must be specified for persistent fields or
	// properties of type java.util.Date and java.util.Calendar
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_TIME")
	private Calendar dateTime;

	private boolean attendance;

	public Appointment() {
		super();
	}

	public Calendar getDateTime() {
		return dateTime;
	}

	public Exam getExam() {
		return this.exam;
	}

	public Seat getSeat() {
		return this.seat;
	}

	public boolean getSetAsideSeat() {
		return this.setAsideSeat;
	}

	public Student getStudent() {
		return this.student;
	}

	public boolean isAttendance() {
		return attendance;
	}

	public void setAttendance(boolean attendance) {
		this.attendance = attendance;
	}

	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public void setSetAsideSeat(boolean setAside) {
		this.setAsideSeat = setAside;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}
