package com.lmsuniversity.examattempt;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import com.lmsuniversity.course.Course;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.Student;
import com.lmsuniversity.announcement.Announcement;

@Entity
@Table(name = "polaganje")
public class ExamAttempt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private double points;
	private int finalGrade;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	@Column(columnDefinition = "LONGTEXT")
	private String note;

	@ManyToOne
	@JoinColumn(name = "predmet_id")
	private Course course;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "obavestenja_id", referencedColumnName = "id")
    private Announcement announcement;

	@ManyToOne
	@JoinColumn(name = "nastavnik_id")
	private Teacher teacher;

	public ExamAttempt() {
		super();
	}

	public ExamAttempt(Long id, double points, int finalGrade, LocalDateTime startTime, LocalDateTime endTime,
			String note, Course course, Student student, Announcement announcement, Teacher teacher) {
		super();
		this.id = id;
		this.points = points;
		this.finalGrade = finalGrade;
		this.startTime = startTime;
		this.endTime = endTime;
		this.note = note;
		this.course = course;
		this.student = student;
		this.announcement = announcement;
		this.teacher = teacher;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Announcement getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}

}
