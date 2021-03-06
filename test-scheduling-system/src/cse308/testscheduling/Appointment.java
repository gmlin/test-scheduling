package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;

import cse308.testscheduling.servlet.DatabaseManager;

/**
 * Entity implementation class for Entity: Appointment
 *
 */
@Entity
public class Appointment implements Serializable, Comparable<Appointment> {

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
	@Column(name = "DATE_TIME")
	private Timestamp dateTime;

	private boolean attendance;


	public Appointment() {
		super();
	}

	public boolean getAttendance() {
		return attendance;
	}

	public String getDateString() {
		LocalDateTime ldt = dateTime.toLocalDateTime();
		return ldt.toLocalDate().toString();
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public Timestamp getEndDateTime() {
		EntityManager em = DatabaseManager.createEntityManager();
		Query query = em.createQuery("SELECT term FROM Term term WHERE term.current = true");
		Term term = (Term) query.getResultList().get(0);
		em.close();
		int gapTime = term.getTestingCenter().getGapTime();
		return Timestamp.valueOf(dateTime.toLocalDateTime().plusMinutes(exam.getDuration() + gapTime));
	}

	public Exam getExam() {
		return this.exam;
	}

	public int getId() {
		return id;
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

	public String getTimeString() {
		LocalDateTime ldt = dateTime.toLocalDateTime();
		return ldt.toLocalTime().toString();
	}

	public boolean isCancelable() {
		if (attendance)
			return false;
		return LocalDateTime.now().plusDays(1).isBefore(dateTime.toLocalDateTime());
	}

	public void setAttendance(boolean attendance) {
		this.attendance = attendance;
	}

	public void setDateTime(Timestamp dateTime) {
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

	@Override
	public int compareTo(Appointment o) {
		return o.getDateTime().compareTo(this.getDateTime());
	}
	public boolean overlapsWith(Timestamp start, int duration) {
		EntityManager em = DatabaseManager.createEntityManager();
		Query query = em.createQuery("SELECT term FROM Term term WHERE term.current = true");
		Term term = (Term) query.getResultList().get(0);
		em.close();
		int gapTime = term.getTestingCenter().getGapTime();
		Timestamp end = Timestamp.valueOf(start.toLocalDateTime().plusMinutes(duration + gapTime));
		if (!this.getDateTime().before(end) || !start.before(this.getEndDateTime())) {
			return false;
		}
		else {
			return true;
		}
	}

	public boolean overlapsWithExam(Timestamp start, Exam exam) {
		if (!this.getExam().equals(exam)) {
			return false;
		}
		else {
			EntityManager em = DatabaseManager.createEntityManager();
			Query query = em.createQuery("SELECT term FROM Term term WHERE term.current = true");
			Term term = (Term) query.getResultList().get(0);
			em.close();
			int gapTime = term.getTestingCenter().getGapTime();
			Timestamp end = Timestamp.valueOf(start.toLocalDateTime().plusMinutes(exam.getDuration() + gapTime));
			if (!this.getDateTime().before(end) || !start.before(this.getEndDateTime())) {
				return false;
			}
			else {
				return true;
			}
		}
	}

	public void sendReminder() throws NamingException {
		User user = this.getStudent().getUser();
		String to = user.getEmail();
		String from = "freyhalltestingcenter@gmail.com";
		try {
			InitialContext ctx = new InitialContext();
			Session session = (Session) ctx.lookup("mail/notifications");
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject("Appointment Reminder");
			String text = "Hello " + user.getFirstName() + " " + user.getLastName() + ".\n"
			+ "You have an appointment for " + this.getExam().getExamId() + " on " 
			+ this.getDateString() + " " + this.getTimeString() + ".\n"
			+ "Your seat number is " + this.getSeat().getSeatNumber() + ".";
			message.setText(text);
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
