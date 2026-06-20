package com.lmsuniversity.course;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.lmsuniversity.examattempt.ExamAttempt;
import com.lmsuniversity.announcement.CourseAnnouncement;
import com.lmsuniversity.teachingmaterial.TeachingMaterial;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.Student;
import com.lmsuniversity.studyprogram.StudyProgram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	@NotBlank
	@Column(nullable = false, unique = true)
	private String courseCode;

	@Column(columnDefinition = "LONGTEXT")
	private String syllabus;

	@NotBlank
	private String name;

	@Min(1)
	private int ects;

	private Date startDate;

	private Date endDate;

	@Column(columnDefinition = "LONGTEXT")
	private String description;

	@OneToMany(mappedBy = "course")
	private Set<TeachingMaterial> teachingMaterials;

	@OneToMany(mappedBy = "course")
	private Set<ExamAttempt> examAttempts;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;

	@ManyToMany(mappedBy = "courses")
	private Set<Student> students;

	@OneToMany(mappedBy = "course")
	private Set<CourseAnnouncement> announcements;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "study_program_id")
	private StudyProgram studyProgram;

	@AssertTrue(message = "endDate must not be before startDate")
	public boolean isDateRangeValid() {
		if (startDate == null || endDate == null) {
			return true;
		}
		return !endDate.before(startDate);
	}
}
