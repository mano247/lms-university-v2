package rs.ac.singidunum.novisad.backend.dto;

public class StudyYearDTO {

	private Long id;

	private int year;

	public StudyYearDTO(Long id, int year) {
		super();
		this.id = id;
		this.year = year;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}


}
