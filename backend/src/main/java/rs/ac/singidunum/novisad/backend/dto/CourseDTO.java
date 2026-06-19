package rs.ac.singidunum.novisad.backend.dto;

import java.util.Date;
import java.util.Set;


public class CourseDTO{
	private Long id;
	private String courseCode;

	private String syllabus;

	private String name;

	private int ects;

	private TeacherDTO teacher;

	private Date startDate;

	private Date endDate;

    private String description;

	private StudyProgramDTO studyProgram;

	private Set<TeachingMaterialDTO> teachingMaterials;

	public CourseDTO() {
		super();
	}

	public CourseDTO(Long id,String courseCode, String syllabus, String name, int ects,
			TeacherDTO teacher, Date startDate, Date endDate, String description,
			Set<TeachingMaterialDTO> teachingMaterials) {
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
		this.teachingMaterials = teachingMaterials;
	}

	public CourseDTO(Long id,String courseCode,StudyProgramDTO studyProgram, String syllabus, String name, int ects,
			TeacherDTO teacher, Date startDate, Date endDate, String description,
			Set<TeachingMaterialDTO> teachingMaterials) {
		super();
		this.id = id;
		this.courseCode = courseCode;
		this.studyProgram = studyProgram;
		this.syllabus = syllabus;
		this.name = name;
		this.ects = ects;
		this.teacher = teacher;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.teachingMaterials = teachingMaterials;
	}

	public CourseDTO(Long id,String courseCode, TeacherDTO teacher, StudyProgramDTO studyProgram, String name, int ects, String description, String syllabus, Set<TeachingMaterialDTO> teachingMaterials) {
		this.id = id;
		this.courseCode = courseCode;
		this.teacher = teacher;
		this.studyProgram = studyProgram;
		this.name = name;
		this.ects = ects;
		this.description = description;
		this.syllabus = syllabus;
		this.teachingMaterials = teachingMaterials;
	}

	public CourseDTO(Long id,String courseCode, TeacherDTO teacher, StudyProgramDTO studyProgram, String name, int ects, String description, String syllabus) {
		this.id = id;
		this.courseCode = courseCode;
		this.teacher = teacher;
		this.studyProgram = studyProgram;
		this.name = name;
		this.ects = ects;
		this.description = description;
		this.syllabus = syllabus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Set<TeachingMaterialDTO> getTeachingMaterials() {
		return teachingMaterials;
	}

	public void setTeachingMaterials(Set<TeachingMaterialDTO> teachingMaterials) {
		this.teachingMaterials = teachingMaterials;
	}

	public StudyProgramDTO getStudyProgram() {
		return studyProgram;
	}

	public void setStudyProgram(StudyProgramDTO studyProgram) {
		this.studyProgram = studyProgram;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
}
