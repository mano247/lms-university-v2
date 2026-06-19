package rs.ac.singidunum.novisad.backend.dto;

import java.util.Set;

public class StudyProgramDTO {

	private Long id;
	private String programCode;
	private String description;

	private String name;
	private String programDirector;
	private FacultyDTO faculty;
	private Set<String> courses;

	public StudyProgramDTO() {
		super();
	}

	public StudyProgramDTO(Long id,String programCode, String description, String name, String programDirector, FacultyDTO faculty, Set<String> courses) {
		super();
		this.id = id;
		this.programCode = programCode;
		this.description = description;
		this.name = name;
		this.programDirector = programDirector;
		this.faculty = faculty;
		this.courses= courses;
	}

	public StudyProgramDTO(Long id,String programCode, String description, String name, String programDirector, FacultyDTO faculty) {
		super();
		this.id = id;
		this.programCode = programCode;
		this.description = description;
		this.name = name;
		this.programDirector = programDirector;
		this.faculty = faculty;
	}

	public StudyProgramDTO(Long id,String programCode, String name) {
		super();
		this.id = id;
		this.programCode = programCode;
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProgramDirector() {
		return programDirector;
	}
	public void setProgramDirector(String programDirector) {
		this.programDirector = programDirector;
	}
	public FacultyDTO getFaculty() {
		return faculty;
	}
	public void setFaculty(FacultyDTO faculty) {
		this.faculty = faculty;
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	public Set<String> getCourses() {
		return courses;
	}

	public void setCourses(Set<String> courses) {
		this.courses = courses;
	}
}
