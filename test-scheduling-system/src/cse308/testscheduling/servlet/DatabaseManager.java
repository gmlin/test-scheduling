package cse308.testscheduling.servlet;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class DatabaseManager
 *
 */
@WebListener
public class DatabaseManager implements ServletContextListener {

	private static EntityManagerFactory emf;
	
    /**
     * Default constructor. 
     */
    public DatabaseManager() {
    }

    public void contextDestroyed(ServletContextEvent arg0) {
		emf.close();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		emf = Persistence.createEntityManagerFactory("test-scheduling-system");
	}
	
    public static EntityManager createEntityManager() {
		return emf.createEntityManager();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllInstances(EntityManager em, Class<T> c) {
		Query query = em.createQuery("SELECT i FROM " + c.getSimpleName() + " i", c);
		return query.getResultList();
	}
}
