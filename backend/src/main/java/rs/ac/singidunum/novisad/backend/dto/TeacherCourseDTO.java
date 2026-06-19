package rs.ac.singidunum.novisad.backend.dto;

import java.util.Date;
import java.util.Set;

public class TeacherCourseDTO{
	private Long id;
	private String courseCode;
	private String syllabus;
	private String name;
	private int ects;
	private TeacherDTO teacher;
	private Date startDate;
	private Date endDate;
    private String description;
	private String studyProgram;
	private Set<TeachingMaterialDTO> teachingMaterials;
	private Set<StudentDTO> students;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getSyllabus() {
		return syllabus;
	}
	public void setSyllabus(String syllabus) {
		this.syllabus = syllabus;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getEcts() {
		return ects;
	}
	public void setEcts(int ects) {
		this.ects = ects;
	}
	public TeacherDTO getTeacher() {
		return teacher;
	}
	public void setTeacher(TeacherDTO teacher) {
		this.teacher = teacher;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStudyProgram() {
		return studyProgram;
	}
	public void setStudyProgram(String studyProgram) {
		this.studyProgram = studyProgram;
	}
	public Set<TeachingMaterialDTO> getTeachingMaterials() {
		return teachingMaterials;
	}
	public void setTeachingMaterials(Set<TeachingMaterialDTO> teachingMaterials) {
		this.teachingMaterials = teachingMaterials;
	}
	public Set<StudentDTO> getStudents() {
		return students;
	}
	public void setStudents(Set<StudentDTO> students) {
		this.students = students;
	}
	public TeacherCourseDTO(Long id, String courseCode, String syllabus, String name, int ects,
			TeacherDTO teacher, Date startDate, Date endDate, String description, String studyProgram,
			Set<TeachingMaterialDTO> teachingMaterials, Set<StudentDTO> students) {
		super();
		this.id = id;
		this.courseCode = courseCode;
		this.syllabus = syllabus;
		this.name = name;
		this.ects = ects;
		this.teacher = teacher;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.studyProgram = studyProgram;
		this.teachingMaterials = teachingMaterials;
		this.students = students;
	}



}
