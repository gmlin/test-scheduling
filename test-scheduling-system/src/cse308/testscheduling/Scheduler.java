package cse308.testscheduling;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timer;

@Singleton
public class Scheduler {
	
	@Schedule(second="*", minute="*", hour="*", persistent=false)
	public void run(Timer timer) {
		System.out.println("testing");
	}
	
	@Schedule(second="*", minute="*", hour="*", persistent=false)
	public void run2(Timer timer) {
		System.out.println("testing2");
	}
}
