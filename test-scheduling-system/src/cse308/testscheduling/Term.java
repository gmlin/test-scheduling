package cse308.testscheduling;

import java.io.Serializable;
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
	private int termid;
	
	private String season;
	
	private int year;
	
	@OneToMany(mappedBy = "term", cascade = CascadeType.ALL)
	private List<Course> courses;
	
	@OneToOne(mappedBy = "term", cascade = CascadeType.ALL)
	private TestingCenter center;
	
	public Term() {
		super();
		setCourses(new ArrayList<Course>());
	}
	
	public void setTermID(int id) {
		termid = id;
	}
	
	public int getTermID () {
		return termid;
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
	
	public void setTestingCenter(TestingCenter c) {
		center = c;
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
	
}
