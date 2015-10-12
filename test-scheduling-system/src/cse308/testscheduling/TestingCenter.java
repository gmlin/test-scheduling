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

	   
	@Id
	private int id;
	private List<Seat> seats;
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
	public List<Seat> getSeats() {
		return this.seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}
   
}
