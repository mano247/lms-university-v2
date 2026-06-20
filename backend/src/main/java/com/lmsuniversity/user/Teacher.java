package com.lmsuniversity.user;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import com.lmsuniversity.examattempt.ExamAttempt;
import com.lmsuniversity.course.Course;
import com.lmsuniversity.rectorate.University;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Teacher extends RegisteredUser {
	private String biography;
	private String personalIdNumber;

	@ManyToOne
	@JoinColumn(name = "university_id")
	private University university;

	@OneToMany(mappedBy = "teacher")
	private Set<Course> courses;

	@OneToMany(mappedBy = "teacher")
	private Set<ExamAttempt> examAttempts;
}
