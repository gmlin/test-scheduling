package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Query;

/**
 * Entity implementation class for Entity: TestingCenter
 *
 */
@Entity
public class TestingCenter implements Serializable {

	private static final long serialVersionUID = 1L;

	// primary key is id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TESTINGCENTER_ID")
	private int id;

	@Column(name = "NUM_SEATS")
	private int numSeats;

	@Column(name = "NUM_SET_ASIDE_SEATS")
	private int numSetAsideSeats;

	@Column(name = "OPEN_TIME")
	private Timestamp openTime;

	@Column(name = "CLOSE_TIME")
	private Timestamp closeTime;

	@Column(name = "CLOSED_DATE_RANGES")
	private List<Date[]> closedDateRanges;

	@Column(name = "RESERVED_PERIODS")
	private List<Calendar[]> reservedPeriods;

	@Column(name = "GAP_TIME")
	private int gapTime;

	@Column(name = "REMINDER_INTERVAL")
	private int reminderInterval;
	
	@Column(name = "CURRENT")
	private boolean current;
	
	@OneToOne
	@JoinColumn(name = "TERM_ID")
	private Term term;

	public TestingCenter() {
		super();
		closedDateRanges = new ArrayList<Date[]>();
		reservedPeriods = new ArrayList<Calendar[]>();
	}

	public void addClosedDateRange(Date[] range) {
		closedDateRanges.add(range);
	}

	public List<Date[]> getClosedDateRanges() {
		return closedDateRanges;
	}

	public Timestamp getCloseTime() {
		return closeTime;
	}
	
	public Time getCloseTimeString(){
		return new Time(closeTime.getHours(), closeTime.getMinutes(), closeTime.getSeconds());
	}
	public Time getOpenTimeString(){
		return new Time(openTime.getHours(), openTime.getMinutes(), openTime.getSeconds());
	}

	public int getGapTime() {
		return gapTime;
	}

	public int getId() {
		return this.id;
	}

	public int getNumSeats() {
		return numSeats;
	}

	public int getNumSetAsideSeats() {
		return numSetAsideSeats;
	}

	public Timestamp getOpenTime() {
		return openTime;
	}

	public int getReminderInterval() {
		return reminderInterval;
	}

	public List<Calendar[]> getReservedPeriods() {
		return reservedPeriods;
	}

	public void setClosedDateRanges(List<Date[]> closedDateRanges) {
		this.closedDateRanges = closedDateRanges;
	}

	public void setCloseTime(Timestamp closeTime) {
		this.closeTime = closeTime;
	}

	public void setGapTime(int gapTime) {
		this.gapTime = gapTime;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNumSeats(int numSeats) {
		this.numSeats = numSeats;
	}

	public void setNumSetAsideSeats(int numSetAsideSeats) {
		this.numSetAsideSeats = numSetAsideSeats;
	}

	public void setOpenTime(Timestamp openTime) {
		this.openTime = openTime;
	}

	public void setReminderInterval(int reminderInterval) {
		this.reminderInterval = reminderInterval;
	}

	public void setReservedPeriods(List<Calendar[]> reservedPeriods) {
		this.reservedPeriods = reservedPeriods;
	}
	
	public void setTerm(Term term) {
		this.term = term;
	}
	
	public long getTotalOpenTime() {
		long milliseconds1 = getOpenTime().getTime();
		long milliseconds2 = getCloseTime().getTime();
		long diff = milliseconds2 - milliseconds1;
		long diffMinutes = diff / (60 * 1000);
		return diffMinutes;
	}
	
	@SuppressWarnings("deprecation")
	public boolean isOpen(Timestamp t) {
		double time = t.getHours() + t.getMinutes() / 60.0;
		double open = openTime.getHours() + openTime.getMinutes() / 60.0;
		double close = closeTime.getHours() + closeTime.getMinutes() / 60.0;
		return (time >= open && time <= close);
	}
	
	@SuppressWarnings("unchecked")
	public boolean isSchedulable(EntityManager em, String examId) {
		/*
		Exam e = em.find(Exam.class, examId);
		Query query = em.createQuery("SELECT appt FROM Appointment appt WHERE appt.dateTime >=:startTime AND appt.dateTime <=:endTime", Appointment.class);
		query.setParameter("startTime", e.getStartDateTime());
		query.setParameter("endTime", e.getEndDateTime());
		List<Appointment> appts = query.getResultList();
		int timeSlots = computeNumTimeslots(e.getStartDateTime(), e.getEndDateTime());
		System.out.println(timeSlots + " total timeslots.");
		for (Appointment appt : appts) {
			timeSlots -= appointmentTimeslots(appt.getExam().getDuration());
		}
		System.out.println(timeSlots + " timeslots left.");
		System.out.println(appointmentTimeslots(e.getDuration()) * e.getStudents().size() + " timeslots needed.");
		return timeSlots >= appointmentTimeslots(e.getDuration()) * e.getStudents().size();
		*/
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean isSchedulable(EntityManager em, int numStudents, Timestamp startDateTime, Timestamp endDateTime, int duration) {
		/*
		 * Query query = em.createQuery("SELECT appt FROM Appointment appt WHERE appt.dateTime >=:startTime AND appt.dateTime <=:endTime", Appointment.class);
		query.setParameter("startTime", startDateTime);
		query.setParameter("endTime", endDateTime);
		List<Appointment> appts = query.getResultList();
		int timeSlots = computeNumTimeslots(startDateTime, endDateTime);
		System.out.println(timeSlots + " total timeslots.");
		for (Appointment appt : appts) {
			timeSlots -= appointmentTimeslots(appt.getExam().getDuration());
		}
		System.out.println(timeSlots + " timeslots left.");
		System.out.println(appointmentTimeslots(duration) * numStudents + " timeslots needed.");
		return timeSlots >= appointmentTimeslots(duration) * numStudents;
		*/
		return true;
	}
	
	public int computeNumTimeslots(double startHour, double endHour) {
		if (startHour >= endHour) {
			return 0;
		}
		double open = openTime.getHours();
		if (openTime.getMinutes() == 30) {
			open += .5;
		}
		else if (openTime.getMinutes() != 0) {
			open += .5 * (openTime.getMinutes() / 30 + 1);
		}
		double close = closeTime.getHours();
		if (closeTime.getMinutes() >= 30) {
			close += 0.5;
		}
		double start;
		double end;
		if (startHour > open) {
			start = startHour;
		}
		else {
			start = open;
		}
		if (endHour > close) {
			end = close;
		}
		else {
			end = endHour;
		}
		return (int)(2 * (end - start));
	}
	@SuppressWarnings("deprecation")
	public int computeNumTimeslots(Timestamp start, Timestamp end) {
		double startHour = start.getHours() + 0.5 * (start.getMinutes() / 30);
		double endHour = end.getHours() + 0.5 * (start.getMinutes() / 30);
		if (start.getDate() == end.getDate() && start.getMonth() == end.getMonth() && start.getYear() == end.getYear()) {
			return (int)(2 * (endHour - startHour));
		}
		int timeSlots = computeNumTimeslots(startHour, endHour);
		start = Timestamp.valueOf(start.toLocalDateTime().plusDays(1));
		double open = openTime.getHours();
		if (openTime.getMinutes() == 30) {
			open += .5;
		}
		else if (openTime.getMinutes() != 0) {
			open += .5 * (openTime.getMinutes() / 30 + 1);
		}
		double close = closeTime.getHours();
		if (closeTime.getMinutes() >= 30) {
			close += 0.5;
		}
		while (!start.toLocalDateTime().plusDays(1).isAfter(end.toLocalDateTime())) {
			timeSlots += computeNumTimeslots(open, close);
			start = Timestamp.valueOf(start.toLocalDateTime().plusDays(1));
		}
		timeSlots += computeNumTimeslots(startHour, endHour);
		return timeSlots;
	}
	
	public int appointmentTimeslots(int duration) {
		duration += gapTime;
		if (duration % 30 == 0) {
			return duration / 30;
		}
		return duration / 30 + 1;
	}
}
