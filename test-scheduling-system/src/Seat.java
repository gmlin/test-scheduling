

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Seat
 *
 */
@Entity

public class Seat implements Serializable {

	   
	@Id
	private int seatNumber;
	private boolean reserved;
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
	public boolean getReserved() {
		return this.reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
   
}
