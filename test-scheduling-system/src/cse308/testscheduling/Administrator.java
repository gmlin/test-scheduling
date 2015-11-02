package cse308.testscheduling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;
import javax.persistence.Query;

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

	// this is a many-to-one association between admin and testing center
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
	
	@SuppressWarnings("unchecked")
	public List<Exam> getPendingExams() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-scheduling-system");
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT exam FROM Exam exam WHERE exam.status = :status",
				Exam.class);
		query.setParameter("status", Status.PENDING);
		List<Exam> pendingExams = null;
		try {
			pendingExams = query.getResultList();
		} catch (NoResultException e) {
		} finally {
			em.close();
			emf.close();
		}
		return pendingExams;
	}

}
