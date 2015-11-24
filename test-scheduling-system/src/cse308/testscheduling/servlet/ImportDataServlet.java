package cse308.testscheduling.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import cse308.testscheduling.Administrator;
import cse308.testscheduling.Appointment;
import cse308.testscheduling.Course;
import cse308.testscheduling.Exam;
import cse308.testscheduling.Instructor;
import cse308.testscheduling.Seat;
import cse308.testscheduling.Student;
import cse308.testscheduling.User;

@WebServlet("/import_data")
@MultipartConfig
public class ImportDataServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	public ImportDataServlet() {
		super();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		HttpSession s = request.getSession();
		int currentid = 100000000;
		
		Part filePart = request.getPart("file1");
		InputStream fileContent = filePart.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent));
		
		Part filePart2 = request.getPart("file2");
		InputStream fileContent2 = filePart2.getInputStream();
		BufferedReader reader2 = new BufferedReader(new InputStreamReader(fileContent2));
		
		Part filePart3 = request.getPart("file3");
		InputStream fileContent3 = filePart3.getInputStream();
		BufferedReader reader3 = new BufferedReader(new InputStreamReader(fileContent3));
		
		Part filePart4 = request.getPart("file4");
		InputStream fileContent4 = filePart4.getInputStream();
		BufferedReader reader4 = new BufferedReader(new InputStreamReader(fileContent4));
		
		String[] parts;
		String line = reader.readLine();
		String line2 = reader2.readLine();
		String line3 = reader3.readLine();
		String line4 = reader4.readLine();
		

		EntityManager em = DatabaseManager.createEntityManager();
		em.getTransaction().begin();
		
		Query queryz = em.createQuery("SELECT student FROM Student student");
		List<Student> currentStudentList = null;
		currentStudentList = queryz.getResultList();
		try{
			for(Student student : currentStudentList){
				if(student.getStudentId() >= currentid){
					currentid = student.getStudentId();
				}
			}
		}
		catch(Exception e){		
		}
		currentid++;
		
		
		em.getTransaction().commit();
		em.getTransaction().begin();
		
		
		//add users (FirstName,LastName,NetID,Email)
		while ((line = reader.readLine()) != null) {
			parts = line.split(",");
			String a = parts[0];
			String b = parts[1];
			String c = parts[2];
			String d = parts[3];
		
			User newUser = new User();
			newUser.setLastName(a);
			newUser.setFirstName(b);
			newUser.setNetId(c);
			newUser.setEmail(d);
			newUser.setPassword("123");
			Student stu = new Student();
			currentid++;
			stu.setStudentId(currentid);
			newUser.setStudent(stu);
			stu.setUser(newUser);
			
			em.persist(newUser);
        }
		
		
		//add instructors (Last Name,First Name,User Id,Email)
		while ((line4 = reader4.readLine()) != null) {
			parts = line4.split(",");
			String a = parts[0];
			String b = parts[1];
			String c = parts[2];
			String d = parts[3];
			
			User newUser = new User();
			newUser.setLastName(a);
			newUser.setFirstName(b);
			newUser.setNetId(c);
			newUser.setEmail(d);
			newUser.setPassword("123");
			Instructor ins = new Instructor();
			newUser.setInstructor(ins);
			ins.setUser(newUser);
			
			em.persist(newUser);
		}
		
		
		
		//add classes (ClassID,Subject,CatalogNumber,Section,InstructorNetID)
		Query queryy = em.createQuery("SELECT instructor FROM Instructor instructor");
		List<Instructor> currentInstructorList = null;
		currentInstructorList = queryy.getResultList();
		while ((line2 = reader2.readLine()) != null) {
			parts = line2.split(",");
			String a = parts[0];
			String b = parts[1];
			String c = parts[2];
			String d = parts[3];
			String f = parts[4];
			
			Course newCourse = new Course();
			newCourse.setCourseId(a);
			newCourse.setSubject(b);
			newCourse.setCatalogNumber(c);
			newCourse.setSection(d);
						
			try{
				for(Instructor instructor : currentInstructorList){
					if(instructor.getUser().getNetId().equals(f)){
						instructor.addCourse(newCourse);
						newCourse.addInstructor(instructor);
						em.persist(instructor);
					}
				}
			} catch(Exception e) {
				
			}
			
			
			
			em.persist(newCourse);
		}
		
		
		//add roster(NetId, ClassID)
		Query queryx = em.createQuery("SELECT student FROM Student student");
		List<Student> updatedStudentList = null;
		updatedStudentList = queryx.getResultList();
		Query query = em.createQuery("SELECT course FROM Course course");
		List<Course> courseList = null;
		courseList = query.getResultList();
		while ((line3 = reader3.readLine()) != null) {
			parts = line3.split(",");
			String a = parts[0];
			String b = parts[1]; 
			
			try{
				for(Student student : updatedStudentList){
					if(student.getUser().getNetId().equals(a)){
						for(Course course : courseList){
							if(course.getCourseId().equals(b)){
								course.addStudent(student);
								student.addCourse(course);
								em.persist(course);
								em.persist(student);
							}
						}
						
					}
				}
				
			} catch (Exception e){
				
			}
	 
		}
		
		
		em.getTransaction().commit();
		em.close();
		s.setAttribute("message", "Data Imported");
		
		reader.close();
		response.sendRedirect(request.getHeader("Referer"));
	}

}
