package cse308.testscheduling;

import cse308.testscheduling.User;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Administrator
 *
 */
@MappedSuperclass

public class Administrator extends User implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public Administrator() {
		super();
	}
   
}
