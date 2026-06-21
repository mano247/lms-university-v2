package com.lmsuniversity.faculty;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.lmsuniversity.rectorate.University;
import com.lmsuniversity.studyprogram.StudyProgram;
import com.lmsuniversity.user.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Faculty {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	@NotBlank
	@Column(nullable = false, unique = true)
	private String facultyCode;

	@NotBlank
	private String name;
	private String contact;

	@Column(columnDefinition = "LONGTEXT")
	private String description;

	@ManyToOne
	@JoinColumn(name = "dean_id")
	private Teacher dean;

	private String image;
	private String address;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "university_id", nullable = false)
	private University university;

	@OneToMany(mappedBy = "faculty")
	private Set<StudyProgram> studyPrograms;
}
