package rs.ac.singidunum.novisad.backend.dto;


public class FinalThesisDTO {
	private Long id;
	private String topic;
	private String link;
	private StudentDTO student;
	private TeacherDTO mentor;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public StudentDTO getStudent() {
		return student;
	}
	public void setStudent(StudentDTO student) {
		this.student = student;
	}
	public TeacherDTO getMentor() {
		return mentor;
	}
	public void setMentor(TeacherDTO mentor) {
		this.mentor = mentor;
	}
	public FinalThesisDTO(Long id, String topic, String link, StudentDTO student, TeacherDTO mentor) {
		super();
		this.id = id;
		this.topic = topic;
		this.link = link;
		this.student = student;
		this.mentor = mentor;
	}


}
