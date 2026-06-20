package com.lmsuniversity.faculty;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import com.lmsuniversity.rectorate.University;
import com.lmsuniversity.studyprogram.StudyProgram;

@Entity
public class Faculty{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String facultyCode;

	private String name;
	private String contact;

	@Column(columnDefinition = "LONGTEXT")
	private String description;

	private String dean;
	private String image;
	private String address;

	@ManyToOne
	@JoinColumn(name = "univerzitet_id", nullable = false)
	private University university;

	@OneToMany(mappedBy = "faculty")
	private Set<StudyProgram> studyPrograms;

	public Faculty() {
		super();
	}

	public Faculty(Long id,String facultyCode, String name, String contact, String description, String dean, String image,
			String address, University university, Set<StudyProgram> studyPrograms) {
		super();
		this.id = id;
		this.setFacultyCode(facultyCode);
		this.name = name;
		this.contact = contact;
		this.description = description;
		this.dean = dean;
		this.image = image;
		this.address = address;
		this.university = university;
		this.studyPrograms = studyPrograms;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDean() {
		return dean;
	}

	public void setDean(String dean) {
		this.dean = dean;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public University getUniversity() {
		return university;
	}

	public void setUniversity(University university) {
		this.university = university;
	}

	public Set<StudyProgram> getStudyPrograms() {
		return studyPrograms;
	}

	public void setStudyPrograms(Set<StudyProgram> studyPrograms) {
		this.studyPrograms = studyPrograms;
	}

	public String getFacultyCode() {
		return facultyCode;
	}

	public void setFacultyCode(String facultyCode) {
		this.facultyCode = facultyCode;
	}

}
