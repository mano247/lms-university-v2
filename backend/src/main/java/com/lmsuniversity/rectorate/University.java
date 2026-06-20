package com.lmsuniversity.rectorate;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.user.Teacher;

@Entity
public class University{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private LocalDateTime foundingDate;
	private String contact;

	@Column(columnDefinition = "LONGTEXT")
	private String description;

	private String image;
	private String address;

	@OneToMany(mappedBy = "university")
	private Set<Faculty> faculties;

	@OneToMany(mappedBy = "university")
	private Set<Teacher> teachers;

	@ManyToOne
    @JoinColumn(name = "rektorat_id")
    @JsonIgnore
    private Rectorate rectorate;

	public University() {
		super();

	}

	public University(Long id, String name, LocalDateTime foundingDate, String contact, String description,
			String image, String address, Set<Faculty> faculties, Set<Teacher> teachers, Rectorate rectorate) {
		super();
		this.id = id;
		this.name = name;
		this.foundingDate = foundingDate;
		this.contact = contact;
		this.description = description;
		this.image = image;
		this.address = address;
		this.faculties = faculties;
		this.teachers = teachers;
		this.rectorate = rectorate;
	}

	public Rectorate getRectorate() {
		return rectorate;
	}

	public void setRectorate(Rectorate rectorate) {
		this.rectorate = rectorate;
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

	public LocalDateTime getFoundingDate() {
		return foundingDate;
	}

	public void setFoundingDate(LocalDateTime foundingDate) {
		this.foundingDate = foundingDate;
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

	public Set<Faculty> getFaculties() {
		return faculties;
	}

	public void setFaculties(Set<Faculty> faculties) {
		this.faculties = faculties;
	}

	public Set<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(Set<Teacher> teachers) {
		this.teachers = teachers;
	}



}
