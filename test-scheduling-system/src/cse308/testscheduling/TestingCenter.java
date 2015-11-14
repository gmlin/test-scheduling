package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	private Time openTime;

	@Column(name = "CLOSE_TIME")
	private Time closeTime;

	@Column(name = "CLOSED_DATE_RANGES")
	private List<Date[]> closedDateRanges;

	@Column(name = "RESERVED_PERIODS")
	private List<Calendar[]> reservedPeriods;

	@Column(name = "GAP_TIME")
	private int gapTime;

	@Column(name = "REMINDER_INTERVAL")
	private int reminderInterval;

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

	public Time getCloseTime() {
		return closeTime;
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

	public Time getOpenTime() {
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

	public void setCloseTime(Time closeTime) {
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

	public void setOpenTime(Time openTime) {
		this.openTime = openTime;
	}

	public void setReminderInterval(int reminderInterval) {
		this.reminderInterval = reminderInterval;
	}

	public void setReservedPeriods(List<Calendar[]> reservedPeriods) {
		this.reservedPeriods = reservedPeriods;
	}
}
