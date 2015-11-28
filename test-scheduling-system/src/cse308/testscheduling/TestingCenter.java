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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
	
	@SuppressWarnings("deprecation")
	public boolean isOpen(Timestamp t) {
		double time = t.getHours() + t.getMinutes() / 60.0;
		double open = openTime.getHours() + openTime.getMinutes() / 60.0;
		double close = closeTime.getHours() + closeTime.getMinutes() / 60.0;
		return (time >= open && time <= close);
	}

}
