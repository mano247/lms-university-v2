package com.lmsuniversity.user;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.lmsuniversity.studyyear.StudyYear;
import com.lmsuniversity.examattempt.ExamAttempt;
import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.course.Course;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString
public class Student extends RegisteredUser {
	@NotBlank
	@Size(max = 20)
	private String indexNumber;

	@ManyToOne
	@JoinColumn(name = "faculty_id")
	private Faculty faculty;

	@ManyToMany
	@JoinTable(
	  name = "student_study_years",
	  joinColumns = @JoinColumn(name = "student_id"),
	  inverseJoinColumns = @JoinColumn(name = "study_year_id"))
	private Set<StudyYear> studyYears;

	@OneToMany(mappedBy = "student")
	private Set<ExamAttempt> examAttempts;

	@ManyToMany
	@JoinTable(
		name = "student_courses",
		joinColumns = @JoinColumn(name = "student_id"),
		inverseJoinColumns = @JoinColumn(name = "course_id"))
	private Set<Course> courses;
}
