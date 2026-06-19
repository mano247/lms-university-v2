package rs.ac.singidunum.novisad.backend.dto;

import java.time.LocalDateTime;

public class ExamAttemptDTO {
	private Long id;
	private double points;
	private int finalGrade;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String note;

	private StudentDTO student;
	private CourseDTO course;
	private TeacherDTO teacher;

	public ExamAttemptDTO() {
		super();
	}

	public ExamAttemptDTO(Long id, double points, int finalGrade, LocalDateTime startTime, LocalDateTime endTime,
			String note, StudentDTO student, CourseDTO course, TeacherDTO teacher) {
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


	public TeacherDTO getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherDTO teacher) {
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
	public StudentDTO getStudent() {
		return student;
	}
	public void setStudent(StudentDTO student) {
		this.student = student;
	}
	public CourseDTO getCourse() {
		return course;
	}
	public void setCourse(CourseDTO course) {
		this.course = course;
	}
}
