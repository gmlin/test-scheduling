package cse308.testscheduling;

import java.io.Serializable;
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
	private int numSeats;
	private int numSetAsideSeats;
	private int gapTime;
	private int reminderInterval;
	
	private static final long serialVersionUID = 1L;

	public TestingCenter() {
		super();
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
   
}
