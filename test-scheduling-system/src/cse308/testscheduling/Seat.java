package cse308.testscheduling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Entity implementation class for Entity: Seat
 *
 */
@Entity

public class Seat implements Serializable {

	private static final long serialVersionUID = 1L;

	// primary key is seatNumber
	@Id
	@Column(name = "SEAT_NUMBER")
	private int seatNumber;
	
	// a seat can be assigned to multiple appointments
	@OneToMany(mappedBy = "seat")
	private List<Appointment> appointments;
	
	@Column(name = "SET_ASIDE")
	private boolean setAside;

	public Seat() {
		super();
		appointments = new ArrayList<Appointment>();
	}

	public void addAppointment(Appointment appointment) {
		appointments.add(appointment);
	}

	public int getSeatNumber() {
		return this.seatNumber;
	}

	public boolean getSetAside() {
		return this.setAside;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	public void setSetAside(boolean setAside) {
		this.setAside = setAside;
	}
}
