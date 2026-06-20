package com.lmsuniversity.examattempt;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import com.lmsuniversity.course.Course;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.Student;
import com.lmsuniversity.announcement.Announcement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exam_attempts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExamAttempt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@Min(0)
	private double points;

	@Min(0)
	private int finalGrade;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	@Column(columnDefinition = "LONGTEXT")
	private String note;

	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "announcement_id", referencedColumnName = "id")
	private Announcement announcement;

	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
}
