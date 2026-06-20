package com.lmsuniversity.user;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import com.lmsuniversity.examattempt.ExamAttempt;
import com.lmsuniversity.course.Course;
import com.lmsuniversity.rectorate.University;

@Entity
public class Teacher extends RegisteredUser{
	private String biography;
	private String personalIdNumber;

	@ManyToOne
	@JoinColumn(name = "univerzitet_id")
	private University university;

	@OneToMany(mappedBy = "teacher")
	private Set<Course> courses;

	@OneToMany(mappedBy = "teacher")
	private Set<ExamAttempt> examAttempts;

	public Teacher() {
		super();
	}

	public Teacher(String firstName, String lastName, String email, String password) {
		super(firstName, lastName, email, password);
	}

	public Teacher(String biography, String personalIdNumber, University university, Set<Course> courses, Set<ExamAttempt> examAttempts) {
		super();
		this.biography = biography;
		this.personalIdNumber = personalIdNumber;
		this.university = university;
		this.courses = courses;
		this.examAttempts = examAttempts;
	}

	public Set<ExamAttempt> getExamAttempts() {
		return examAttempts;
	}

	public void setExamAttempts(Set<ExamAttempt> examAttempts) {
		this.examAttempts = examAttempts;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getPersonalIdNumber() {
		return personalIdNumber;
	}

	public void setPersonalIdNumber(String personalIdNumber) {
		this.personalIdNumber = personalIdNumber;
	}

	public University getUniversity() {
		return university;
	}

	public void setUniversity(University university) {
		this.university = university;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

}
