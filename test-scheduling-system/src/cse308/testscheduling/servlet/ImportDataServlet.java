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
import cse308.testscheduling.Exam;
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
		
		//String a = request.getParameter("file1");
		//String b = request.getParameter("file2");
		//String c = request.getParameter("file3");
		//System.out.println(a);
		//System.out.println(b);
		//System.out.println(c);
		
		Part filePart = request.getPart("file1");
		InputStream fileContent = filePart.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent));
		
		String line;
		String[] parts;
		line = reader.readLine();
		int currentid = 100000003;
		
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-scheduling-system");
		EntityManager em = emf.createEntityManager();
		List<Query> addStudents = new ArrayList<Query>();
		em.getTransaction().begin();
		
		try {
			for (Query query : addStudents)
				query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		em.getTransaction().commit();
		em.getTransaction().begin();
		
		
		while ((line = reader.readLine()) != null) {
			parts = line.split(",");
			String a = parts[0];
			String b = parts[1];
			String c = parts[2];
			String d = parts[3];
		
			User newUser = new User();
			newUser.setFirstName(a);
			newUser.setLastName(b);
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
		
	
		
		
		em.getTransaction().commit();
		em.close();
		emf.close();
		
	
		
		
		reader.close();
		
		response.sendRedirect(request.getHeader("Referer"));
	}

}
