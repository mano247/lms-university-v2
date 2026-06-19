package rs.ac.singidunum.novisad.backend.model;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "obavestenja")
public class Announcement {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime date;

	@Column(columnDefinition = "LONGTEXT")
	private String content;

	private String title;

	private String image;

	private Date startDate;

	private Date endDate;


	public Announcement() {
		super();
	}

	public Announcement(Long id,  String content, String title, LocalDateTime date, String image, Date startDate, Date endDate) {
		super();
		this.id = id;
		this.date = date;
		this.content = content;
		this.title = title;
		this.image = image;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Announcement(Long id, String title, String content, LocalDateTime date, String image) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.date = date;
		this.image = image;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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
}
