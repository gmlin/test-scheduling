package cse308.testscheduling;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DatabaseManager implements ServletContextListener {
	private static EntityManagerFactory emf;

	public static EntityManager createEntityManager() {
		return emf.createEntityManager();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		emf.close();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		emf = Persistence.createEntityManagerFactory("test-scheduling-system");
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllInstances(EntityManager em, Class<T> c) {
		Query query = em.createQuery("SELECT i FROM " + c.getSimpleName() + " i", c);
		return query.getResultList();
	}
}
