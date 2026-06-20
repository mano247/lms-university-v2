package com.lmsuniversity.studyprogram;

import java.util.Set;

import com.lmsuniversity.faculty.FacultyDto;

public class StudyProgramDto {

	private Long id;
	private String programCode;
	private String description;

	private String name;
	private String programDirector;
	private FacultyDto faculty;
	private Set<String> courses;

	public StudyProgramDto() {
		super();
	}

	public StudyProgramDto(Long id,String programCode, String description, String name, String programDirector, FacultyDto faculty, Set<String> courses) {
		super();
		this.id = id;
		this.programCode = programCode;
		this.description = description;
		this.name = name;
		this.programDirector = programDirector;
		this.faculty = faculty;
		this.courses= courses;
	}

	public StudyProgramDto(Long id,String programCode, String description, String name, String programDirector, FacultyDto faculty) {
		super();
		this.id = id;
		this.programCode = programCode;
		this.description = description;
		this.name = name;
		this.programDirector = programDirector;
		this.faculty = faculty;
	}

	public StudyProgramDto(Long id,String programCode, String name) {
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
	public FacultyDto getFaculty() {
		return faculty;
	}
	public void setFaculty(FacultyDto faculty) {
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
