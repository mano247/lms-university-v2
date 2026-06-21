package com.lmsuniversity.studyprogram;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.course.Course;
import com.lmsuniversity.user.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "study_programs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StudyProgram {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	@NotBlank
	@Column(nullable = false, unique = true)
	private String programCode;

	@Column(columnDefinition = "LONGTEXT")
	private String description;

	@NotBlank
	private String name;

	@ManyToOne
	@JoinColumn(name = "program_director_id")
	private Teacher programDirector;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "faculty_id", nullable = false)
	private Faculty faculty;

	@OneToMany(mappedBy = "studyProgram")
	private Set<Course> courses;
}
