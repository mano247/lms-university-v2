package com.lmsuniversity.user;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import com.lmsuniversity.studyyear.StudyYear;
import com.lmsuniversity.permission.Permission;
import com.lmsuniversity.examattempt.ExamAttempt;
import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.course.Course;

@Entity
public class Student extends RegisteredUser{
	private String indexNumber;

	@ManyToOne
	@JoinColumn(name="fakultet_id")
	Faculty faculty;

	@ManyToMany
	@JoinTable(
	  name = "studentNaGodini",
	  joinColumns = @JoinColumn(name = "student_id"),
	  inverseJoinColumns = @JoinColumn(name = "godinaStudija_id"))
	Set<StudyYear> studyYears;

	@OneToMany(mappedBy = "student")
	private Set<ExamAttempt> examAttempts;

	@ManyToMany
    @JoinTable(
        name = "studentiPredmeti",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "predmet_id"))
    private Set<Course> courses;


	public Student() {
		super();
	}

	public Student(Long id, String username, String email, String password, Set<Permission> permissions, String indexNumber, Faculty faculty) {
		super(id, username, email, password, permissions);
		this.indexNumber = indexNumber;
		this.faculty = faculty;
	}

	public Student(Long id, String firstName, String lastName, String email, String password, Set<Permission> permissions, String indexNumber, Set<Course> courses, Faculty faculty) {
		super(id, firstName, lastName, email, password, permissions);
		this.indexNumber = indexNumber;
		this.faculty = faculty;
		this.courses = courses;
	}

	public Student(Long id, String firstName, String lastName, String email, String password, Set<Permission> permissions, String indexNumber, Set<Course> courses, Set<ExamAttempt> examAttempts, Faculty faculty) {
		super(id, firstName, lastName, email, password, permissions);
		this.indexNumber = indexNumber;
		this.faculty = faculty;
		this.courses = courses;
		this.examAttempts = examAttempts;
	}

	public Student(Long id, String firstName, String lastName, String email, String password, Set<Permission> permissions, String indexNumber, Set<Course> courses, Set<ExamAttempt> examAttempts, Set<StudyYear> studyYears, Faculty faculty) {
		super(id, firstName, lastName, email, password, permissions);
		this.indexNumber = indexNumber;
		this.faculty = faculty;
		this.courses = courses;
		this.examAttempts = examAttempts;
		this.studyYears = studyYears;
	}

	public String getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}

	public Set<StudyYear> getStudyYears() {
		return studyYears;
	}

	public void setStudyYears(Set<StudyYear> studyYears) {
		this.studyYears = studyYears;
	}

	public Set<ExamAttempt> getExamAttempts() {
		return examAttempts;
	}

	public void setExamAttempts(Set<ExamAttempt> examAttempts) {
		this.examAttempts = examAttempts;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	@Override
	public String toString() {
		return "Student [indexNumber=" + indexNumber + ", faculty=" + faculty + ", studyYears=" + studyYears
				+ ", examAttempts=" + examAttempts + ", courses=" + courses + "]";
	}
}
