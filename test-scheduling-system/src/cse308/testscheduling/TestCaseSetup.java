package cse308.testscheduling;

import java.sql.Time;
import java.sql.Timestamp;
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
		clearTableQueries.add(em.createNativeQuery("DELETE FROM appointment"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM course_instructor"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM course_student"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM term"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM course"));
		clearTableQueries.add(em.createNativeQuery("DELETE FROM administrator"));
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
		} catch (Exception e) {
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

		Term term1 = new Term();
		term1.setTermID(1158);
		term1.setSeason("Fall");
		term1.setYear(2015);
		term1.setCurrent(true);
		
		Term term2 = new Term();
		term2.setTermID(1161);
		term2.setSeason("Winter");
		term2.setYear(2016);
		term2.setCurrent(false);

		
		TestingCenter t = new TestingCenter();
		t.setNumSeats(64);
		t.setNumSetAsideSeats(4);
		t.setOpenTime(new Timestamp(0, 0, 0, 8, 0, 0, 0));
		t.setCloseTime(new Timestamp(0, 0, 0, 20, 0, 0, 0));
		t.setGapTime(10);
		t.setReminderInterval(30);
		t.setTerm(term1);
		term1.setTestingCenter(t);
		
		TestingCenter t2 = new TestingCenter();
		t2.setNumSeats(100);
		t2.setNumSetAsideSeats(10);
		t2.setOpenTime(new Timestamp(0, 0, 0, 7, 0, 0, 0));
		t2.setCloseTime(new Timestamp(0, 0, 0, 19, 0, 0, 0));
		t2.setGapTime(10);
		t2.setReminderInterval(30);
		t2.setTerm(term2);
		term2.setTestingCenter(t2);
		
		User u2 = new User();
		u2.setNetId("instructor");
		u2.setPassword("123");
		u2.setFirstName("Instructor");
		u2.setLastName("Test");
		u2.setEmail("test@test.com");
		Instructor instructor = new Instructor();
		u2.setInstructor(instructor);
		instructor.setUser(u2);

		User u3 = new User();
		u3.setNetId("student1");
		u3.setPassword("123");
		u3.setFirstName("Student");
		u3.setLastName("One");
		u3.setEmail("test@test.com");
		Student student1 = new Student();
		student1.setStudentId(100000000);
		u3.setStudent(student1);
		student1.setUser(u3);

		User u4 = new User();
		u4.setNetId("student2");
		u4.setPassword("123");
		u4.setFirstName("Student");
		u4.setLastName("Two");
		u4.setEmail("test@test.com");
		Student student2 = new Student();
		student2.setStudentId(100000001);
		u4.setStudent(student2);
		student2.setUser(u4);
		
		User u5 = new User();
		u5.setNetId("all");
		u5.setPassword("123");
		u5.setFirstName("Test");
		u5.setLastName("User");
		u5.setEmail("test@test.com");
		Student student3 = new Student();
		student3.setStudentId(100000002);
		u5.setStudent(student3);
		student3.setUser(u5);
		Instructor instructor2 = new Instructor();
		u5.setInstructor(instructor2);
		instructor2.setUser(u5);
		Administrator admin2 = new Administrator();
		u5.setAdministrator(admin2);
		admin2.setUser(u5);

		Course c1 = new Course();
		c1.setCourseId("11111-1158");
		c1.setSubject("CSE");
		c1.setCatalogNumber("308");
		c1.setSection("01");
		student1.addCourse(c1);
		student2.addCourse(c1);
		student3.addCourse(c1);
		instructor.addCourse(c1);
		instructor2.addCourse(c1);
		c1.addInstructor(instructor);
		c1.addInstructor(instructor2);
		c1.addStudent(student1);
		c1.addStudent(student2);
		c1.addStudent(student3);
		c1.setTerm(term1);
		term1.addCourse(c1);

		Course c2 = new Course();
		c2.setCourseId("22222-1158");
		c2.setSubject("CSE");
		c2.setCatalogNumber("310");
		c2.setSection("02");
		student1.addCourse(c2);
		student2.addCourse(c2);
		student3.addCourse(c2);
		instructor.addCourse(c2);
		instructor2.addCourse(c2);
		c2.addInstructor(instructor);
		c2.addInstructor(instructor2);
		c2.addStudent(student1);
		c2.addStudent(student2);
		c2.addStudent(student3);
		c2.setTerm(term1);
		term1.addCourse(c2);
		
		Course c3 = new Course();
		c3.setCourseId("11111-1161");
		c3.setSubject("CSE");
		c3.setCatalogNumber("308");
		c3.setSection("01");
		student1.addCourse(c3);
		student2.addCourse(c3);
		student3.addCourse(c3);
		instructor.addCourse(c3);
		instructor2.addCourse(c3);
		c3.addInstructor(instructor);
		c3.addInstructor(instructor2);
		c3.addStudent(student1);
		c3.addStudent(student2);
		c3.addStudent(student3);
		c3.setTerm(term2);
		term2.addCourse(c3);

		Seat seat;
		for (int i = 0; i < t2.getNumSeats(); i++) {
			seat = new Seat();
			seat.setSetAside(false);
			em.persist(seat);
		}
		for (int i = 0; i < t2.getNumSetAsideSeats(); i++) {
			seat = new Seat();
			seat.setSetAside(true);
			em.persist(seat);
		}

		em.persist(u1);
		em.persist(u2);
		em.persist(u3);
		em.persist(u4);
		em.persist(u5);
		em.persist(c1);
		em.persist(c2);
		em.persist(c3);
		em.persist(t);
		em.persist(term1);
		em.persist(term2);

		em.getTransaction().commit();

		em.close();
		emf.close();
	}

}
