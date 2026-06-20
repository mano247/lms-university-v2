package com.lmsuniversity.faculty;

import com.lmsuniversity.rectorate.UniversityDto;

public class FacultyDto {

	private Long id;
	private String facultyCode;
	private String name;
	private String contact;
	private String description;

	private String dean;
	private String image;
	private String address;

	private UniversityDto university;


	public FacultyDto(Long id,String facultyCode, String name, String contact, String description, String dean, String image,String address,
			UniversityDto university) {
		super();
		this.id = id;
		this.facultyCode=facultyCode;
		this.name = name;
		this.contact = contact;
		this.description = description;
		this.dean = dean;
		this.image = image;
		this.address = address;
		this.university = university;
	}

	public FacultyDto(Long id,String facultyCode, String name) {
		super();
		this.id = id;
		this.facultyCode=facultyCode;
		this.name = name;
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

	public UniversityDto getUniversity() {
		return university;
	}

	public void setUniversity(UniversityDto university) {
		this.university = university;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFacultyCode() {
		return facultyCode;
	}

	public void setFacultyCode(String facultyCode) {
		this.facultyCode = facultyCode;
	}



}
