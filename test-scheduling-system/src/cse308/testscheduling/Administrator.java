package cse308.testscheduling;

import cse308.testscheduling.User;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Administrator
 *
 */
@Entity

public class Administrator implements Serializable {

	@Id
	private int ID;
	@OneToOne(mappedBy="administrator",optional=false)
	private User user;

	private static final long serialVersionUID = 1L;

	public Administrator() {
		super();
	}

}
