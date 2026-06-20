package com.lmsuniversity.studentyearenrollment;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.lmsuniversity.studyyear.StudyYear;
import com.lmsuniversity.studyprogram.StudyProgram;
import com.lmsuniversity.user.Student;

@Entity
public class StudentYearEnrollment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Date enrollmentDate;

	@ManyToOne
	@JoinColumn(name = "godinaStudija_id")
	private StudyYear studyYear;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@ManyToOne
	@JoinColumn(name = "studijski_program_id")
	private StudyProgram studyProgram;

	public StudentYearEnrollment() {
		super();
	}

	public StudentYearEnrollment(Long id, Date enrollmentDate, StudyYear studyYear, Student student,
			StudyProgram studyProgram) {
		super();
		this.id = id;
		this.enrollmentDate = enrollmentDate;
		this.studyYear = studyYear;
		this.student = student;
		this.studyProgram = studyProgram;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public StudyYear getStudyYear() {
		return studyYear;
	}

	public void setStudyYear(StudyYear studyYear) {
		this.studyYear = studyYear;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public StudyProgram getStudyProgram() {
		return studyProgram;
	}

	public void setStudyProgram(StudyProgram studyProgram) {
		this.studyProgram = studyProgram;
	}



}
