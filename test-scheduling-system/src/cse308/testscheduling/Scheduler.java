package cse308.testscheduling;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timer;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import cse308.testscheduling.servlet.DatabaseManager;

@Singleton
public class Scheduler {
	
	@SuppressWarnings({"unchecked"})
	@Schedule(second="0", minute="0,30", hour="*")
	public void updateExamStatus(Timer timer) {
		EntityManager em = DatabaseManager.createEntityManager();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("SELECT exam FROM Exam exam WHERE exam.startDateTime =:start");
			Timestamp now = Timestamp.from(timer.getNextTimeout().toInstant().minus(30, ChronoUnit.MINUTES));
			System.out.println(now);
			query.setParameter("start", now);
			List<Exam> startingExams = query.getResultList();
			for (Exam exam : startingExams) {
				if (exam.getStatus() == Status.APPROVED) {
					exam.setStatus(Status.ONGOING);
				}
				else {
					exam.setStatus(Status.CANCELED);
				}
			}
			query = em.createQuery("SELECT exam FROM Exam exam WHERE exam.endDateTime =:end AND exam.status =:status");
			query.setParameter("end", now);
			query.setParameter("status", Status.ONGOING);
			List<Exam> endingExams = query.getResultList();
			for (Exam exam : endingExams) {
				exam.setStatus(Status.COMPLETED);
			}
			em.getTransaction().commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			em.close();
		}
	}
	
}
