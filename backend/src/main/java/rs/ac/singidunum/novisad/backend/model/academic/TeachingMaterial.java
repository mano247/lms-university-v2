package rs.ac.singidunum.novisad.backend.model.academic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import rs.ac.singidunum.novisad.backend.model.Outcome;

@Entity
public class TeachingMaterial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String authors;
	private String publicationYear;
	private String publisher;

	@Column(columnDefinition = "LONGTEXT")
	private String description;

	private String url;
	private int pageCount;
	private int quantity;
	private int issuedQuantity;

	private Outcome outcome;

	@ManyToOne
	@JoinColumn(name = "predmet_id")
	private Course course;


	public TeachingMaterial() {
		super();
	}

	public TeachingMaterial(Long id, String title, String authors, String publicationYear, String publisher, String description,
			String url, Outcome outcome, Course course,int quantity,int issuedQuantity) {
		super();
		this.id = id;
		this.title = title;
		this.authors = authors;
		this.publicationYear = publicationYear;
		this.publisher = publisher;
		this.description = description;
		this.url = url;
		this.outcome = outcome;
		this.course = course;
		this.quantity = quantity;
		this.issuedQuantity = issuedQuantity;
	}

	public TeachingMaterial(Long id, String title, String authors, int pageCount, String publisher, String description) {
		super();
		this.id = id;
		this.title = title;
		this.authors = authors;
		this.pageCount = pageCount;
		this.publisher = publisher;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Outcome getOutcome() {
		return outcome;
	}

	public void setOutcome(Outcome outcome) {
		this.outcome = outcome;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getIssuedQuantity() {
		return issuedQuantity;
	}

	public void setIssuedQuantity(int issuedQuantity) {
		this.issuedQuantity = issuedQuantity;
	}


}
