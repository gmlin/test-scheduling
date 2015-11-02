package cse308.testscheduling;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Entity implementation class for Entity: Instructor
 *
 */
@Entity

public class Instructor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Instructor.class.getName());

	// primary key is Id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// this is a one-to-one association between instructor and user,
	// instructor field specifies the role of user
	@OneToOne(optional = false)
	@JoinColumn(name = "NET_ID")
	private User user;

	/*
	 * //a instructor can associate with multiple exams
	 * 
	 * @OneToMany(mappedBy="instructor") private List<Exam> exams;
	 */

	// a course has multiple instructors, instructor can teach multiple courses
	@ManyToMany(mappedBy = "instructors")
	private List<Course> courses;

	// a instructor can have multiple ad hoc exams
	@OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
	private List<Exam> adHocExams;

	public Instructor() {
		super();
		setCourses(new ArrayList<Course>());
		adHocExams = new ArrayList<Exam>();
	}

	public void addCourse(Course course) {
		getCourses().add(course);
	}
	/*
	 * public List<Request> getRequests() { return requests; } public void
	 * setRequests(List<Request> requests) { this.requests = requests; }
	 */
	/*
	 * public List<Exam> getExams() { return exams; } public void
	 * setExams(List<Exam> exams) { this.exams = exams; }
	 */

	public List<Course> getCourses() {
		return courses;
	}

	public User getUser() {
		return user;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Exam> getAdHocExams() {
		return adHocExams;
	}

	public void setAdHocExams(List<Exam> adHocExams) {
		this.adHocExams = adHocExams;
	}

	public void addAdHocExam(Exam adHocExam) {
		adHocExams.add(adHocExam);
	}

	public void requestCourseExam(String courseId, int duration, Timestamp startDateTime, Timestamp endDateTime) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-scheduling-system");
		EntityManager em = emf.createEntityManager();
		logger.entering(getClass().getName(), "requestCourseExam");
		File f = new File("/CourseExamRequestTest.txt");
		FileHandler fh = null;
		try {
			fh = new FileHandler("CourseExamRequestTest.txt");
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		logger.addHandler(fh);
		try {
			em.getTransaction().begin();
			Exam exam = new Exam();
			exam.setStatus(Status.PENDING);
			exam.setAdHoc(false);
			Course course = em.find(Course.class, courseId);
			exam.setCourse(course);
			course.addExam(exam);
			exam.setExamId(course.getCourseId() + "_ex" + String.valueOf(course.getExams().size()));
			exam.setDuration(duration);
			exam.setStartDateTime(startDateTime);
			exam.setEndDateTime(endDateTime);
			em.persist(exam);
			em.getTransaction().commit();
			logger.log(Level.INFO, "Regular Exam Sucessfully Requested for " + course.getCourseId() + 
					" . Duration is: " + duration +
					". StartDate is " + startDateTime +
					". EndDate is " + endDateTime);
		}
		catch(Exception e) {
			logger.log(Level.SEVERE, "Error in making Course Exam", e);
			throw e;
			
		}
		finally {
			em.close();
			emf.close();
			logger.exiting(getClass().getName(), "requestCourseExam");
		}
	}

	public void requestAdHocExam(String[] netIds, int duration, Timestamp startDateTime, Timestamp endDateTime) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-scheduling-system");
		logger.entering(getClass().getName(), "requestAdHocExam");
		File f = new File("/AdHocExamRequestTest.txt");
		FileHandler fh = null;
		try {
			fh = new FileHandler("AdHocExamRequestTest.txt");
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		logger.addHandler(fh);
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			Exam exam = new Exam();
			exam.setStatus(Status.PENDING);
			exam.setAdHoc(true);
			Query query = em.createQuery("SELECT e FROM Exam e WHERE e.adHoc = true", Exam.class);
			exam.setExamId("adhoc_ex" + (query.getResultList().size() + 1));
			exam.setDuration(duration);
			exam.setStartDateTime(startDateTime);
			exam.setEndDateTime(endDateTime);
			User u;
			for (String netId : netIds) {
				query = em.createQuery("SELECT u FROM User u WHERE u.netId = :username", User.class);
				query.setParameter("username", netId.trim());
				u = (User)(query.getSingleResult());
				u.getStudent().addAdHocExam(exam);
				exam.addStudent(u.getStudent());
			}
			exam.setInstructor(this);
			this.addAdHocExam(exam);
			em.persist(exam);
			em.getTransaction().commit();
			logger.log(Level.INFO, "AdHoc Exam Sucessfully Requested" + 
					" . Duration is: " + duration +
					". StartDate is " + startDateTime +
					". EndDate is " + endDateTime);
		}
		catch(Exception e) {
			logger.log(Level.SEVERE, "Error in making AdHoc Exam", e);
			throw e;
		}
		finally {
			em.close();
			emf.close();
			logger.exiting(getClass().getName(), "requestAdHocExam");
		}
	}

}
