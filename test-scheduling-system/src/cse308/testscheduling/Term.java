package cse308.testscheduling;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;


@Entity

public class Term implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "TERM_ID")
	private int termID;
	
	private String season;
	
	private int year;
	
	@OneToMany(mappedBy = "term", cascade = CascadeType.ALL)
	private List<Course> courses;
	
	@OneToOne(mappedBy = "term", cascade = CascadeType.ALL)
	private TestingCenter testingCenter;
	
	private boolean current;
	
	public Term() {
		super();
		setCourses(new ArrayList<Course>());
	}
	
	
	public void setTermID(int id) {
		termID = id;
	}
	
	public int getTermID () {
		return termID;
	}
	public String getSeason() {
		return season;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setSeason(String s) {
		season = s;
	}
	
	public void setYear(int y) {
		year = y;
	}
	
	public List<Course> getCourses() {
		return courses;
	}
	
	public void addCourse(Course c) {
		getCourses().add(c);
	}
	
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	public String toString() {
		return String.valueOf(termID);
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public TestingCenter getTestingCenter() {
		return testingCenter;
	}

	public void setTestingCenter(TestingCenter testingCenter) {
		this.testingCenter = testingCenter;
	}
}
