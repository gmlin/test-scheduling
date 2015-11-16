package cse308.testscheduling;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity

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
