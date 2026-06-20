package com.lmsuniversity.studyprogram;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.course.Course;

@Entity
@Table(name = "studijski_programi")
public class StudyProgram {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String programCode;

	@Column(columnDefinition = "LONGTEXT")
    private String description;

	private String name;
	private String programDirector;

	@ManyToOne
	@JoinColumn(name = "fakultet_id", nullable = false)
	private Faculty faculty;

	@OneToMany(mappedBy = "studyProgram")
	private Set<Course> courses;


	public StudyProgram() {
		super();
	}

	public StudyProgram(Long id,String programCode,  String description, String name, String programDirector,
			Faculty faculty, Set<Course> courses) {
		super();
		this.id = id;
		this.programCode = programCode;
		this.description = description;
		this.name = name;
		this.programDirector = programDirector;
		this.faculty = faculty;
		this.courses = courses;
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
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

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
}
