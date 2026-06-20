package com.lmsuniversity.studentyearenrollment;

import java.sql.Date;

import com.lmsuniversity.studyyear.StudyYearDto;
import com.lmsuniversity.studyprogram.StudyProgramDto;
import com.lmsuniversity.user.StudentDto;

public class StudentYearEnrollmentDto {

	private Long id;
	private Date enrollmentDate;
	private StudyYearDto studyYear;
	private StudentDto student;
	private StudyProgramDto studyProgram;



	public StudentYearEnrollmentDto() {
		super();
	}

	public StudentYearEnrollmentDto(Long id, Date enrollmentDate, StudyYearDto studyYear, StudentDto student,
			StudyProgramDto studyProgram) {
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
	public StudyYearDto getStudyYear() {
		return studyYear;
	}
	public void setStudyYear(StudyYearDto studyYear) {
		this.studyYear = studyYear;
	}
	public StudentDto getStudent() {
		return student;
	}
	public void setStudent(StudentDto student) {
		this.student = student;
	}
	public StudyProgramDto getStudyProgram() {
		return studyProgram;
	}
	public void setStudyProgram(StudyProgramDto studyProgram) {
		this.studyProgram = studyProgram;
	}

}
