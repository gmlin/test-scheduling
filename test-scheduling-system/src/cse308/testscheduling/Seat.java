package cse308.testscheduling;


import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity implementation class for Entity: Seat
 *
 */
@Entity

public class Seat implements Serializable {

	//primary key is seatNumber  
	@Id
	@Column(name="SEAT_NUMBER")
	private int seatNumber;
	
	//a seat can be assigned to multiple appointments
	@OneToMany(mappedBy="seat")
	private List<Appointment> appointments;
	@Column(name="SET_ASIDE")
	private boolean setAside;
	private static final long serialVersionUID = 1L;

	public Seat() {
		super();
		appointments = new ArrayList<Appointment>();
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
   
	public void addAppointment(Appointment appointment) {
		appointments.add(appointment);
	}
}
