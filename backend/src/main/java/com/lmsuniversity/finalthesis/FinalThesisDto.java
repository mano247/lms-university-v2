package com.lmsuniversity.finalthesis;

import com.lmsuniversity.user.StudentDto;
import com.lmsuniversity.user.TeacherDto;

public class FinalThesisDto {
	private Long id;
	private String topic;
	private String link;
	private StudentDto student;
	private TeacherDto mentor;
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
	public StudentDto getStudent() {
		return student;
	}
	public void setStudent(StudentDto student) {
		this.student = student;
	}
	public TeacherDto getMentor() {
		return mentor;
	}
	public void setMentor(TeacherDto mentor) {
		this.mentor = mentor;
	}
	public FinalThesisDto(Long id, String topic, String link, StudentDto student, TeacherDto mentor) {
		super();
		this.id = id;
		this.topic = topic;
		this.link = link;
		this.student = student;
		this.mentor = mentor;
	}


}
