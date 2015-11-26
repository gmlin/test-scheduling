package cse308.testscheduling;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

public class Term implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "TERM_ID")
	private int id;
	
	private String season;
	
	private int year;
	
	public Term() {
		super();
		
	}
	
	public Term(int id) {
		super();
		this.id = id;
		if (id % 10 == 1) {
			season = "WINTER";
		}
		else if (id % 10 == 4) {
			season = "SPRING";
		}
		else if (id % 10 == 6) {
			season = "SUMMER";
		}
		else if (id % 10 == 8) {
			season = "FALL";
		}
		year = 1900 + id / 10;
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
}
