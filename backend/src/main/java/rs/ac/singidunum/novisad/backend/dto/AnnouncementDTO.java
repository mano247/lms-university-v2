package rs.ac.singidunum.novisad.backend.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class AnnouncementDTO {

	private Long id;

	private LocalDateTime postedAt;

	private String content;

	private String title;

	private String image;

	private Date startDate;

	private Date endDate;

	public AnnouncementDTO() {
		super();
	}

	public AnnouncementDTO(Long id, LocalDateTime postedAt, String content, String title, String image, Date startDate, Date endDate) {
		super();
		this.id = id;
		this.postedAt = postedAt;
		this.content = content;
		this.title = title;
		this.image = image;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(LocalDateTime postedAt) {
		this.postedAt = postedAt;
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
