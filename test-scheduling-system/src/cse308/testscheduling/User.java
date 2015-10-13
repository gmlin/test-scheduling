package cse308.testscheduling;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity

public class User implements Serializable {

	//name the primary key column "NET_ID"	   
	@Id
	@Column(name ="NET_ID")
	private String username;
	private String password;
	private String name;
	
	//this is a one-to-one association between admin and user, 
	//admin field specifies the role of user
	@OneToOne(optional=true)
	private Administrator administrator;
	
	//this is a one-to-one association between instructor and user, 
	//instructor field specifies the role of user
	@OneToOne(mappedBy="user",optional=true)
	private Instructor instructor;
	
	//this is a one-to-one association between student and user, 
	//student field specifies the role of user
	@OneToOne(optional=true)
	private Student student;
	private static final long serialVersionUID = 1L;

	public User() {
		super();
	}   
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}   
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isAdministrator(){
		return administrator!=null;
	}
	public boolean isInstructor(){
		return instructor!=null;
	}
	public boolean isStudent(){
		return student!=null;
	}
}
