package com.lmsuniversity.examattempt;

import java.time.LocalDateTime;

import com.lmsuniversity.course.CourseDto;
import com.lmsuniversity.user.StudentDto;
import com.lmsuniversity.user.TeacherDto;

public class ExamAttemptDto {
	private Long id;
	private double points;
	private int finalGrade;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String note;

	private StudentDto student;
	private CourseDto course;
	private TeacherDto teacher;

	public ExamAttemptDto() {
		super();
	}

	public ExamAttemptDto(Long id, double points, int finalGrade, LocalDateTime startTime, LocalDateTime endTime,
			String note, StudentDto student, CourseDto course, TeacherDto teacher) {
		super();
		this.id = id;
		this.points = points;
		this.finalGrade = finalGrade;
		this.startTime = startTime;
		this.endTime = endTime;
		this.note = note;
		this.student = student;
		this.course = course;
		this.teacher = teacher;
	}


	public TeacherDto getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherDto teacher) {
		this.teacher = teacher;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public double getPoints() {
		return points;
	}
	public void setPoints(double points) {
		this.points = points;
	}
	public int getFinalGrade() {
		return finalGrade;
	}
	public void setFinalGrade(int finalGrade) {
		this.finalGrade = finalGrade;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public StudentDto getStudent() {
		return student;
	}
	public void setStudent(StudentDto student) {
		this.student = student;
	}
	public CourseDto getCourse() {
		return course;
	}
	public void setCourse(CourseDto course) {
		this.course = course;
	}
}
