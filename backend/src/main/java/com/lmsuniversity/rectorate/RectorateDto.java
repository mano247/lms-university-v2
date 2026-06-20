package com.lmsuniversity.rectorate;

import java.util.Set;

public class RectorateDto {

	private Long id;
	private String name;
	private String contact;
	private String image;
	private String address;
	private String rectorName;


	private Set<UniversityDto> universities;

	public RectorateDto() {
		super();
	}

	public RectorateDto(Long id, String name, String contact, String image, String address,String rectorName,
			Set<UniversityDto> universities) {
		super();
		this.id = id;
		this.name = name;
		this.contact = contact;
		this.image = image;
		this.address = address;
		this.rectorName = rectorName;
		this.universities = universities;
	}

	public RectorateDto(Long id, String name, String contact, String image, String address,String rectorName) {
		super();
		this.id = id;
		this.name = name;
		this.contact = contact;
		this.image = image;
		this.address = address;
		this.rectorName = rectorName;
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

	public Set<UniversityDto> getUniversities() {
		return universities;
	}

	public void setUniversities(Set<UniversityDto> universities) {
		this.universities = universities;
	}

	public String getRectorName() {
		return rectorName;
	}

	public void setRectorName(String rectorName) {
		this.rectorName = rectorName;
	}


}
