package com.lmsuniversity.studyyear;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.lmsuniversity.user.Student;

@Entity
public class StudyYear {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private int year;



	public StudyYear() {
		super();
	}

	public StudyYear(Long id, int year,
			Set<Student> students) {
		super();
		this.id = id;
	}

	public StudyYear(Long id, int year) {
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
