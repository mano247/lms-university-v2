package rs.ac.singidunum.novisad.backend.dto;

import java.time.LocalDateTime;

public class UniversityDTO {
	private Long id;
	private String name;
	private LocalDateTime foundingDate;
	private String contact;
	private String rector;
	private String address;
	private String description;
	private String image;
    private RectorateDTO rectorate;

	public UniversityDTO() {
		super();
	}

	public UniversityDTO(Long id, String name, LocalDateTime foundingDate, String contact, String description,
			String image, String address, RectorateDTO rectorate) {
		this.id = id;
		this.name = name;
		this.foundingDate = foundingDate;
		this.contact = contact;
		this.description = description;
		this.image = image;
		this.address = address;
		this.rectorate=rectorate;;
	}

	public UniversityDTO(Long id, String name, LocalDateTime foundingDate, String contact, String description,
			String image, String address) {
		this.id = id;
		this.name = name;
		this.foundingDate = foundingDate;
		this.contact = contact;
		this.description = description;
		this.image = image;
		this.address = address;
	}

	public UniversityDTO(Long id, String name) {
		this.id = id;
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

	public String getRector() {
		return rector;
	}

	public void setRector(String rector) {
		this.rector = rector;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public RectorateDTO getRectorate() {
		return rectorate;
	}

	public void setRectorate(RectorateDTO rectorate) {
		this.rectorate = rectorate;
	}



}
