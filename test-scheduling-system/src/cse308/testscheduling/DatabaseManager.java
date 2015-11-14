package cse308.testscheduling;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DatabaseManager implements ServletContextListener {
	private static EntityManagerFactory emf;
	
	public static EntityManager createEntityManager() {
		return emf.createEntityManager();
	}
	
    public void contextDestroyed(ServletContextEvent arg0)  { 
    	emf.close();
    }

    public void contextInitialized(ServletContextEvent arg0)  { 
    	emf = Persistence.createEntityManagerFactory("test-scheduling-system");
    }
}
