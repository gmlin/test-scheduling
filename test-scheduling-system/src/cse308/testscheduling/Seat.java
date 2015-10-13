package cse308.testscheduling;


import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * Entity implementation class for Entity: Seat
 *
 */
@Entity

public class Seat implements Serializable {

	   
	@Id
	private int seatNumber;
	@OneToMany(mappedBy="seat")
	private List<Appointment> appointments;
	private boolean setAside;
	private static final long serialVersionUID = 1L;

	public Seat() {
		super();
	}   
	public int getSeatNumber() {
		return this.seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}   
	public boolean getSetAside() {
		return this.setAside;
	}
	public void setSetAside(boolean setAside) {
		this.setAside = setAside;
	}
   
}
