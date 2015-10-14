package cse308.testscheduling;

import cse308.testscheduling.User;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Administrator
 * testing
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

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TESTING_CENTER")
	private TestingCenter testingCenter;
	
	private static final long serialVersionUID = 1L;

	public Administrator() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TestingCenter getTestingCenter() {
		return testingCenter;
	}

	public void setTestingCenter(TestingCenter testingCenter) {
		this.testingCenter = testingCenter;
	}

}
