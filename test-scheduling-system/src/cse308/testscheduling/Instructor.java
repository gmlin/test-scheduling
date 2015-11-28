package cse308.testscheduling;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import cse308.testscheduling.servlet.DatabaseManager;

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

	public void addAdHocExam(Exam adHocExam) {
		adHocExams.add(adHocExam);
	}

	public void addCourse(Course course) {
		getCourses().add(course);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instructor other = (Instructor) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public boolean cancelRequest(EntityManager em, String examId) {
		//EntityManager em = DatabaseManager.createEntityManager();
		em.getTransaction().begin();
		try {
			Exam exam = em.find(Exam.class, examId);
			if (exam == null || !this.hasPermission(exam))
				return false;
			if (exam.getStatus() == Status.PENDING || exam.getStatus() == Status.DENIED) {
				exam.setStatus(Status.CANCELED);
			}
			else
				return false;
			em.getTransaction().commit();
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			//em.close();
		}
		return true;
	}
	
	public List<Exam> getAdHocExams() {
		return adHocExams;
	}

	public List<Exam> getAllExams() {
		List<Exam> exams = new ArrayList<Exam>();
		for (Course course : this.getCourses()) {
			for (Exam exam : course.getExams()) {
				exams.add(exam);
			}
		}
		for (Exam exam : this.getAdHocExams()) {
			exams.add(exam);
		}
		Collections.sort(exams);
		return exams;
	}
	
	public List<Course> getCourses() {
		return courses;
	}
	
	public List<Exam> getCurrentRequests() {
		List<Exam> examRequests = new ArrayList<Exam>();
		for (Course course : this.getCourses()) {
			for (Exam exam : course.getExams()) {
				if (exam.getStatus() == Status.APPROVED || exam.getStatus() == Status.DENIED) {
					examRequests.add(exam);
				}
			}
		}
		for (Exam exam : this.getAdHocExams()) {
			if (exam.getStatus() == Status.APPROVED || exam.getStatus() == Status.DENIED) {
				examRequests.add(exam);
			}
		}
		Collections.sort(examRequests);
		return examRequests;
	}
	
	public User getUser() {
		return user;
	}

	public boolean hasPermission(Exam exam) {
		if (exam.getAdHoc()){
			return this.equals(exam.getInstructor());
		} else {
			return exam.getCourse().getInstructors().contains(this);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	public boolean requestAdHocExam(EntityManager em, String[] netIds, int duration, Timestamp startDateTime, Timestamp endDateTime) {
		logger.entering(getClass().getName(), "requestAdHocExam");
		File f = new File("/AdHocExamRequestTest.log");
		FileHandler fh = null;
		try {
			fh = new FileHandler("AdHocExamRequestTest.log");
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		logger.addHandler(fh);
		if (endDateTime.before(startDateTime) 
				|| startDateTime.toLocalDateTime().isBefore(LocalDateTime.now())
				|| startDateTime.toLocalDateTime().plusMinutes(duration).isAfter(endDateTime.toLocalDateTime()))
			return false;
		//EntityManager em = DatabaseManager.createEntityManager();
		em.getTransaction().begin();
		try {
			Exam exam = new Exam();
			exam.setStatus(Status.PENDING);
			exam.setAdHoc(true);
			exam.setExamId("adhoc_" + this.getUser().getNetId() + "_ex" + (this.getAdHocExams().size() + 1));
			exam.setDuration(duration);
			exam.setStartDateTime(startDateTime);
			exam.setEndDateTime(endDateTime);
			User u;
			Query query;
			for (String netId : netIds) {
				System.out.println(netId);
				u = em.find(User.class, netId.trim());
				u.getStudent().addAdHocExam(exam);
				exam.addStudent(u.getStudent());
			}
			exam.setInstructor(this);
			this.addAdHocExam(exam);
			em.persist(exam);
			em.getTransaction().commit();
			logger.log(Level.INFO, "AdHoc Exam Sucessfully Requested" + " . Duration is: " + duration
					+ ". StartDate is " + startDateTime + ". EndDate is " + endDateTime);
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error in making AdHoc Exam", e);
			throw e;
		} finally {
			//em.close();
			logger.exiting(getClass().getName(), "requestAdHocExam");
		}
	}

	public boolean requestCourseExam(EntityManager em, String courseId, int duration, Timestamp startDateTime, Timestamp endDateTime) {
		logger.entering(getClass().getName(), "requestCourseExam");
		File f = new File("/CourseExamRequestTest.log");
		FileHandler fh = null;
		try {
			fh = new FileHandler("CourseExamRequestTest.log");
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		logger.addHandler(fh);
		if (endDateTime.before(startDateTime) 
				|| startDateTime.toLocalDateTime().isBefore(LocalDateTime.now())
				|| startDateTime.toLocalDateTime().plusMinutes(duration).isAfter(endDateTime.toLocalDateTime()))
			return false;
		//EntityManager em = DatabaseManager.createEntityManager();
		try {
			em.getTransaction().begin();
			Exam exam = new Exam();
			exam.setStatus(Status.PENDING);
			exam.setAdHoc(false);
			Course course = em.find(Course.class, courseId);
			exam.setCourse(course);
			course.addExam(exam);
			exam.setExamId(course + "_ex" + String.valueOf(course.getExams().size()));
			exam.setDuration(duration);
			exam.setStartDateTime(startDateTime);
			exam.setEndDateTime(endDateTime);
			em.persist(exam);
			em.getTransaction().commit();
			logger.log(Level.INFO, "Regular Exam Sucessfully Requested for " + course.getCourseId() + " . Duration is: "
					+ duration + ". StartDate is " + startDateTime + ". EndDate is " + endDateTime);
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error in making Course Exam", e);
			throw e;

		} finally {
			//em.close();
			logger.exiting(getClass().getName(), "requestCourseExam");
		}
	}

	public void setAdHocExams(List<Exam> adHocExams) {
		this.adHocExams = adHocExams;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return user.getFirstName() + " " + user.getLastName();
	}
	
	public List<Exam> getTermExams(int termId) {
		List<Exam> exams = getAllExams();
		List<Exam> termExams = new ArrayList<Exam>();
		for (Exam exam : exams) {
			if (exam.getTerm().getTermID() == termId) {
				termExams.add(exam);
			}
		}
		return termExams;
	}
}
