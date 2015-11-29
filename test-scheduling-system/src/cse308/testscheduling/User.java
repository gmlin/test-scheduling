package cse308.testscheduling;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NoResultException;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import cse308.testscheduling.servlet.DatabaseManager;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	// name the primary key column "NET_ID"
	@Id
	@Column(name = "NET_ID")
	private String netId;

	private String password;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	private String email;

	// aggregation used so that user can have multiple permission states
	// as multiple inheritance is not possible in java

	// this is an optional one-to-one association between admin and user,
	// admin field specifies the role of user
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = true)
	private Administrator administrator;

	// this is an optional one-to-one association between instructor and user,
	// instructor field specifies the role of user
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = true)
	private Instructor instructor;

	// this is an optional association between student and user,
	// student field specifies the role of user
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = true)
	private Student student;

	public User() {
		super();
	}

	public Administrator getAdministrator() {
		return administrator;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public String getLastName() {
		return lastName;
	}

	public String getNetId() {
		return this.netId;
	}

	public String getPassword() {
		return this.password;
	}

	public Student getStudent() {
		return student;
	}

	public void setAdministrator(Administrator administrator) {
		this.administrator = administrator;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setNetId(String netId) {
		this.netId = netId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
	public Term getCurrentTerm() {
		EntityManager em = DatabaseManager.createEntityManager();
		Query query = em.createQuery("SELECT term FROM Term term WHERE term.current = true");
		Term term = (Term) query.getResultList().get(0);
		em.close();
		return term;
	}

	public List<Term> getAllTerms(){
		EntityManager em = DatabaseManager.createEntityManager();
		List<Term> terms = DatabaseManager.getAllInstances(em, Term.class);
		em.close();
		return terms;
	}
}
