package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TestingCenter
 *
 */
@Entity

public class TestingCenter implements Serializable {

	//primary key is id   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	@Column(name="NUM_SEATS")
	private int numSeats;
	@Column(name="NUM_SET_ASIDE_SEATS")
	private int numSetAsideSeats;
	@Column(name="OPEN_TIME")
	private Time openTime;
	@Column(name="CLOSE_TIME")
	private Time closeTime;
	@Column(name="CLOSED_DATE_RANGES")
	private List<Date[]> closedDateRanges;
	@Column(name="RESERVED_PERIODS")
	private List<Calendar[]> reservedPeriods;
	@Column(name="GAP_TIME")
	private int gapTime;
	@Column(name="REMINDER_INTERVAL")
	private int reminderInterval;
	private List<Administrator> administrators;
	
	private static final long serialVersionUID = 1L;

	public TestingCenter() {
		super();
		closedDateRanges = new ArrayList<Date[]>();
		reservedPeriods = new ArrayList<Calendar[]>();
		setAdministrators(new ArrayList<Administrator>());
	}   
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}   

	public int getNumSeats() {
		return numSeats;
	}
	public void setNumSeats(int numSeats) {
		this.numSeats = numSeats;
	}
	public int getNumSetAsideSeats() {
		return numSetAsideSeats;
	}
	public void setNumSetAsideSeats(int numSetAsideSeats) {
		this.numSetAsideSeats = numSetAsideSeats;
	}
	public int getGapTime() {
		return gapTime;
	}
	public void setGapTime(int gapTime) {
		this.gapTime = gapTime;
	}
	public int getReminderInterval() {
		return reminderInterval;
	}
	public void setReminderInterval(int reminderInterval) {
		this.reminderInterval = reminderInterval;
	}
	public Time getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Time openTime) {
		this.openTime = openTime;
	}
	public Time getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Time closeTime) {
		this.closeTime = closeTime;
	}
	public List<Date[]> getClosedDateRanges() {
		return closedDateRanges;
	}
	public void setClosedDateRanges(List<Date[]> closedDateRanges) {
		this.closedDateRanges = closedDateRanges;
	}
	public void addClosedDateRange(Date[] range) {
		closedDateRanges.add(range);
	}
	public List<Calendar[]> getReservedPeriods() {
		return reservedPeriods;
	}
	public void setReservedPeriods(List<Calendar[]> reservedPeriods) {
		this.reservedPeriods = reservedPeriods;
	}
	public List<Administrator> getAdministrators() {
		return administrators;
	}
	public void setAdministrators(List<Administrator> administrators) {
		this.administrators = administrators;
	}
	public void addAdministrators(Administrator administrator) {
		administrators.add(administrator);
	}
	
}
