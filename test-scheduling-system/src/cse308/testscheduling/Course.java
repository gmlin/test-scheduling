package cse308.testscheduling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 * Entity implementation class for Entity: Course
 *
 */
@Entity

public class Course implements Serializable {

	private static final long serialVersionUID = 1L;

	// The primary key in nameed "COURSE_ID" in course table
	@Id
	@Column(name = "COURSE_ID")
	private String courseId;
	
	@Column
	private String subject;
	
	@Column
	private String catalogNumber;
	
	@Column
	private String section;

	// a course has multiple students, student can have multiple courses
	// generate new table contains the course id column and student net id
	// column
	@ManyToMany
	@JoinTable(name = "COURSE_STUDENT", joinColumns = {
			@JoinColumn(name = "COURSE_ID", referencedColumnName = "COURSE_ID") }, inverseJoinColumns = {
					@JoinColumn(name = "NET_ID", referencedColumnName = "NET_ID") })
	private List<Student> students;

	// a course has multiple instructors, instructor can teach multiple courses
	// generate new table contains the course id column and instructor net id
	// column
	@ManyToMany
	@JoinTable(name = "COURSE_INSTRUCTOR", joinColumns = {
			@JoinColumn(name = "COURSE_ID", referencedColumnName = "course_ID") }, inverseJoinColumns = {
					@JoinColumn(name = "NET_ID", referencedColumnName = "NET_ID") })
	private List<Instructor> instructors;

	// a course can have multiple exam, so it is one-to-many
	// the mappedBy element indicates that this is the nonowning side of
	// the association.
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<Exam> exams;
	
	public Course() {
		super();
		setStudents(new ArrayList<Student>());
		setInstructors(new ArrayList<Instructor>());
	}

	public void addExam(Exam exam) {
		exams.add(exam);
	}

	public void addInstructor(Instructor instructor) {
		getInstructors().add(instructor);
	}

	public void addStudent(Student student) {
		getStudents().add(student);
	}

	public String getCourseId() {
		return this.courseId;
	}

	public List<Exam> getExams() {
		return exams;
	}

	public List<Instructor> getInstructors() {
		return instructors;
	}

	public List<Student> getStudents() {
		return students;
	}
	
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}

	public void setInstructors(List<Instructor> instructors) {
		this.instructors = instructors;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	
	public String toString() {
		return subject + getCatalogNumber() + "-" + getSection();
	}

	public String getCatalogNumber() {
		return catalogNumber;
	}

	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
}
