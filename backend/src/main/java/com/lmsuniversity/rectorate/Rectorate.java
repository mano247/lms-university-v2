package com.lmsuniversity.rectorate;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Rectorate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String contact;
	private String image;
	private String address;
	private String rectorName;

	@OneToMany(mappedBy = "rectorate")
    private Set<University> universities;

	public Rectorate() {
		super();
	}

	public Rectorate(Long id, String name, String rectorName, String contact, String image, String address, Set<University> universities) {
		super();
		this.id = id;
		this.name = name;
		this.rectorName = rectorName;
		this.contact = contact;
		this.image = image;
		this.address = address;
		this.universities = universities;
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

	public Set<University> getUniversities() {
		return universities;
	}

	public void setUniversities(Set<University> universities) {
		this.universities = universities;
	}

	public String getRectorName() {
		return rectorName;
	}

	public void setRectorName(String rectorName) {
		this.rectorName = rectorName;
	}
}
