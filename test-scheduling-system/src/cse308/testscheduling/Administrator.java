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
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	//id is the primary key in admin table
	private int id;
	
	//this is a one-to-one association between admin and user, 
	//admin field specifies the role of user
	@OneToOne(optional=false)
	@JoinColumn(name="NET_ID")
	private User user;

	private static final long serialVersionUID = 1L;

	public Administrator() {
		super();
	}

}
