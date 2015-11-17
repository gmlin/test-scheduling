package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import cse308.testscheduling.servlet.DatabaseManager;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int seatNumber;

	// a seat can be assigned to multiple appointments
	@OneToMany(mappedBy = "seat", cascade = CascadeType.ALL)
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

	public boolean checkAdjacent(Timestamp t, Exam e) {
		EntityManager em = DatabaseManager.createEntityManager();
		Seat seat1 = em.find(Seat.class, seatNumber - 1);
		Seat seat2 = em.find(Seat.class, seatNumber + 1);
		if ((seat1 != null && e.equals(seat1.examAt(t))) || (seat2 != null && e.equals(seat2.examAt(t)))) {
			em.close();
			return false;
		}
		em.close();
		return true;
	}

	public Exam examAt(Timestamp t) {
		for (Appointment appt : appointments) {
			if (appt.getDateTime().compareTo(t) <= 0 || appt.getEndDateTime().compareTo(t) >= 0) {
				return appt.getExam();
			}
		}
		return null;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}
	
	public int getSeatNumber() {
		return this.seatNumber;
	}

	public boolean getSetAside() {
		return this.setAside;
	}

	@SuppressWarnings("deprecation")
	public boolean isAppointable(Timestamp t, Exam e) {
		if (t.getMinutes() % 30 != 0) {
			return false;
		}
		if (appointments.isEmpty()) {
			return true;
		}
		if (examAt(t) != null) {
			return false;
		}
		else {
			return checkAdjacent(t, e);
		}
	}

	public void setSetAside(boolean setAside) {
		this.setAside = setAside;
	}
}