package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.OneToOne;
import javax.persistence.Query;

import cse308.testscheduling.servlet.DatabaseManager;

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

	public List<Exam> getApprovedExams() {
		EntityManager em = DatabaseManager.createEntityManager();
		Query query = em.createQuery("SELECT exam FROM Exam exam WHERE exam.status = :status", Exam.class);
		query.setParameter("status", Status.APPROVED);
		List<Exam> approvedExams = null;
		try {
			approvedExams = query.getResultList();
		} catch (NoResultException e) {
		} finally {
			em.close();
		}
		return approvedExams ;
	}

	@SuppressWarnings("unchecked")
	public List<Exam> getPendingExams() {
		EntityManager em = DatabaseManager.createEntityManager();
		Query query = em.createQuery("SELECT exam FROM Exam exam WHERE exam.status = :status", Exam.class);
		query.setParameter("status", Status.PENDING);
		List<Exam> pendingExams = null;
		try {
			pendingExams = query.getResultList();
		} catch (NoResultException e) {
		} finally {
			em.close();
		}
		return pendingExams;
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

	public Appointment makeAppointment(String studentId, String examId, Timestamp dateTime, Boolean setAside) {
		EntityManager em = DatabaseManager.createEntityManager();
		try {
			Student student = em.find(Student.class, studentId);
			return student.makeAppointment(examId, dateTime, setAside);
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			em.close();
		}
	}

}
