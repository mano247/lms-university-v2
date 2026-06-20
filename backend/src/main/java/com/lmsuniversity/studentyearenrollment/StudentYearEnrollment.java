package com.lmsuniversity.studentyearenrollment;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import com.lmsuniversity.studyyear.StudyYear;
import com.lmsuniversity.studyprogram.StudyProgram;
import com.lmsuniversity.user.Student;
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
public class StudentYearEnrollment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@NotNull
	private Date enrollmentDate;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "study_year_id")
	private StudyYear studyYear;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "study_program_id")
	private StudyProgram studyProgram;
}
