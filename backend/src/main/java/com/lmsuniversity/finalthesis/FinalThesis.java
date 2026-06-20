package com.lmsuniversity.finalthesis;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.Student;

@Entity
public class FinalThesis {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String topic;

	private String link;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@ManyToOne
	@JoinColumn(name = "mentor_id")
	private Teacher mentor;


	public FinalThesis(Long id, String topic, String link, Student student, Teacher mentor) {
		super();
		this.id = id;
		this.topic = topic;
		this.link = link;
		this.student = student;
		this.mentor = mentor;
	}

	public FinalThesis() {
		super();
	}

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

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Teacher getMentor() {
		return mentor;
	}

	public void setMentor(Teacher mentor) {
		this.mentor = mentor;
	}
}
