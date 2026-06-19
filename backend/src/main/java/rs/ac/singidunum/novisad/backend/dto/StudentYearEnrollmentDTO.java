package rs.ac.singidunum.novisad.backend.dto;

import java.sql.Date;


public class StudentYearEnrollmentDTO {

	private Long id;
	private Date enrollmentDate;
	private StudyYearDTO studyYear;
	private StudentDTO student;
	private StudyProgramDTO studyProgram;



	public StudentYearEnrollmentDTO() {
		super();
	}

	public StudentYearEnrollmentDTO(Long id, Date enrollmentDate, StudyYearDTO studyYear, StudentDTO student,
			StudyProgramDTO studyProgram) {
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
	public StudyYearDTO getStudyYear() {
		return studyYear;
	}
	public void setStudyYear(StudyYearDTO studyYear) {
		this.studyYear = studyYear;
	}
	public StudentDTO getStudent() {
		return student;
	}
	public void setStudent(StudentDTO student) {
		this.student = student;
	}
	public StudyProgramDTO getStudyProgram() {
		return studyProgram;
	}
	public void setStudyProgram(StudyProgramDTO studyProgram) {
		this.studyProgram = studyProgram;
	}

}
