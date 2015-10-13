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
	//ID is the primary key in admin table
	private int ID;
	
	//this is a one-to-one association between admin and user, 
	//admin field specifies the role of user
	@OneToOne(mappedBy="administrator",optional=false)
	private User user;

	private static final long serialVersionUID = 1L;

	public Administrator() {
		super();
	}

}
