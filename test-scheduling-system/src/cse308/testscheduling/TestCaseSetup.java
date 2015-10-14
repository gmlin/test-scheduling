package cse308.testscheduling;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class TestCaseSetup {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-scheduling-system");
		EntityManager em = emf.createEntityManager();
		List<Query> clearTableQueries = new ArrayList<Query>();
		clearTableQueries.add(em.createNativeQuery("DELETE FROM administrator"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM appointment"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM course_instructor"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM course_student"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM course"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM exam"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM instructor"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM seat"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM student"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM testingcenter"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM user"));
		em.getTransaction().begin();
		try {
			for (Query query : clearTableQueries)
				query.executeUpdate();
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		
		User u1 = new User();
		u1.setNetId("admin");
		u1.setPassword("123");
		u1.setFirstName("Admin");
		u1.setLastName("Test");
		u1.setEmail("test@test.com");
		
		Administrator admin = new Administrator();
		u1.setAdministrator(admin);
		admin.setUser(u1);
		
		TestingCenter t = new TestingCenter();
		t.setNumSeats(64);
		t.setNumSetAsideSeats(4);
		t.setOpenTime(new Time(8, 0, 0));
		t.setCloseTime(new Time(20, 0, 0));
		t.setGapTime(10);
		t.setReminderInterval(30);
		t.addAdministrators(admin);
		admin.setTestingCenter(t);
		
		User u2 = new User();
		u2.setNetId("instructor");
		u2.setPassword("123");
		u2.setFirstName("Instructor");
		u2.setLastName("Test");
		u1.setEmail("test@test.com");
		Instructor instructor = new Instructor();
		u2.setInstructor(instructor);
		instructor.setUser(u2);

		User u3 = new User();
		u3.setNetId("student");
		u3.setPassword("123");
		u3.setFirstName("Student");
		u3.setLastName("Test");
		u1.setEmail("test@test.com");
		Student student = new Student();
		student.setStudentId(100000000);
		u3.setStudent(student);
		student.setUser(u3);
		
		Course c1 = new Course();
		c1.setCourseId("CSE308-01_1158");
		student.addCourse(c1);
		instructor.addCourse(c1);
		c1.addInstructor(instructor);
		c1.addStudent(student);
		
		Course c2 = new Course();
		c2.setCourseId("CSE310-01_1158");
		student.addCourse(c2);
		instructor.addCourse(c2);
		c2.addInstructor(instructor);
		c2.addStudent(student);
		
		em.persist(u1);
		em.persist(u2);
		em.persist(u3);
		em.persist(c1);
		em.persist(c2);
		em.persist(t);
		
		em.getTransaction().commit();
		
		em.close();
		emf.close();
	}

}
