package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.CascadeType;

import cse308.testscheduling.servlet.DatabaseManager;

/**
 * Entity implementation class for Entity: Seat
 *
 */
@Entity

public class Seat implements Serializable, Comparable<Seat> {

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
		em.close();
		if (seat1 != null) {
			for (Appointment appt :seat1.getAppointments()) {
				if (appt.overlapsWith(t, e.getDuration())) {
					return false;
				}
			}
		}
		if (seat2 != null) {
			for (Appointment appt :seat2.getAppointments()) {
				if (appt.overlapsWithExam(t, e)) {
					return false;
				}
			}
		}
		
		return true;
	}

	public Exam examAt(Timestamp t) {
		EntityManager em = DatabaseManager.createEntityManager();
		Query query = em.createQuery("SELECT term FROM Term term WHERE term.current = true");
		Term term = (Term) query.getResultList().get(0);
		em.close();
		int gapTime = term.getTestingCenter().getGapTime();
		for (Appointment appt : appointments) {
			Timestamp end = Timestamp.valueOf(appt.getEndDateTime().toLocalDateTime().plusMinutes(gapTime));
			if (appt.getDateTime().compareTo(t) <= 0 || end.compareTo(t) >= 0) {
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
	public boolean isAppointable(Timestamp t, Exam e) throws Exception {
		LocalDateTime apptEnd = t.toLocalDateTime().plusMinutes(e.getDuration());
		if (t.before(e.getStartDateTime())
				|| apptEnd.isAfter(e.getEndDateTime().toLocalDateTime())) {
			throw new Exception("Invalid appointment times.");
		}
		EntityManager em = DatabaseManager.createEntityManager();
		Query query = em.createQuery("SELECT term FROM Term term WHERE term.current = true");
		Term term = (Term) query.getResultList().get(0);
		em.close();
		TestingCenter tc = term.getTestingCenter();
		if (!tc.isOpen(t) || !tc.isOpen(Timestamp.valueOf(t.toLocalDateTime().plusMinutes(e.getDuration())))) {
			throw new Exception("Testing center is not open for entirety of appointment.");
		}
		if (checkAdjacent(t, e)) {
			if (appointments.isEmpty()) {
				return true;
			}
			for (Appointment appt : appointments) {
				if (appt.overlapsWith(t, e.getDuration())) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}

	public void setSetAside(boolean setAside) {
		this.setAside = setAside;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + seatNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seat other = (Seat) obj;
		if (seatNumber != other.seatNumber)
			return false;
		return true;
	}
	@Override
	public int compareTo(Seat o) {
		return this.seatNumber - o.seatNumber;
	}
}