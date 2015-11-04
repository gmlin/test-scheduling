package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;

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

	public void setSetAside(boolean setAside) {
		this.setAside = setAside;
	}
	
	public Exam examAt(Timestamp t) {
		for (Appointment appt : appointments) {
			if (appt.getDateTime().compareTo(t) <= 0 ||
					appt.getEndDateTime().compareTo(t) >= 0) {
				return appt.getExam();
			}
		}
		return null;
	}
	
	public boolean checkAdjacent(Timestamp t, Exam e) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-scheduling-system");
		EntityManager em = emf.createEntityManager();
		Seat seat1 = em.find(Seat.class, seatNumber-1);
		Seat seat2 = em.find(Seat.class, seatNumber+1);
		if ((seat1 != null && e.equals(seat1.examAt(t))) ||
				(seat2 != null && e.equals(seat2.examAt(t)))) {
			em.close();
			emf.close();
			System.out.println("fasfas");
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean isAppointable(Timestamp t, Exam e) {
		if (t.getMinutes() % 30 != 0) {
			System.out.println("fasfasdsdss");

			return false;
		}
		if (appointments.isEmpty()) {
			System.out.println("yaaay");
			return true;
		}
		for (Appointment appt : appointments) {
			if (appt.getDateTime().compareTo(t) > 0 ||
					appt.getEndDateTime().compareTo(t) < 0) {
				if (checkAdjacent(t,e)) {
					return true;
				}
			}
		}
		System.out.println("fadsds434234sfas");

		return false;
	}
}