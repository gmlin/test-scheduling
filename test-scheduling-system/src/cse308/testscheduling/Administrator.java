package cse308.testscheduling;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Entity implementation class for Entity: Administrator
 */
@Entity

public class Administrator implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// id is the primary key in admin table
	private int id;

	// this is a one-to-one association between admin and user,
	// admin field specifies the role of user
	@OneToOne(optional = false)
	@JoinColumn(name = "NET_ID")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TESTINGCENTER_ID")
	private TestingCenter testingCenter;

	public Administrator() {
		super();
	}

	public TestingCenter getTestingCenter() {
		return testingCenter;
	}

	public User getUser() {
		return user;
	}

	public void setTestingCenter(TestingCenter testingCenter) {
		this.testingCenter = testingCenter;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
