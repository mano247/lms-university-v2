package rs.ac.singidunum.novisad.backend.model.academic;

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

import rs.ac.singidunum.novisad.backend.model.ExamAttempt;
import rs.ac.singidunum.novisad.backend.model.CourseAnnouncement;
import rs.ac.singidunum.novisad.backend.model.user.Teacher;
import rs.ac.singidunum.novisad.backend.model.user.Student;

@Entity
@Table(name = "predmeti")
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String courseCode;

	@Column(columnDefinition = "LONGTEXT")
    private String syllabus;

	private String name;

	private int ects;

	private Date startDate;

	private Date endDate;

	@Column(columnDefinition = "LONGTEXT")
	private String description;

	@OneToMany(mappedBy = "course")
	private Set<TeachingMaterial> teachingMaterials;

	@OneToMany(mappedBy = "course")
	private Set<ExamAttempt> examAttempts;

	@ManyToOne
    @JoinColumn(name = "nastavnik_id")
    private Teacher teacher;

	@ManyToMany(mappedBy = "courses")
    private Set<Student> students;

	@OneToMany(mappedBy = "course")
    private Set<CourseAnnouncement> announcements;

	@ManyToOne
    @JoinColumn(name = "studijskiProgram_id")
    private StudyProgram studyProgram;

	public Course() {
		super();
	}

	public Course(Long id,String courseCode, String syllabus, String name, int ects,
			Date startDate, Date endDate, String description
			, Set<ExamAttempt> examAttempts, Teacher teacher,
			Set<Student> students, StudyProgram studyProgram,Set<TeachingMaterial> teachingMaterials, Set<CourseAnnouncement> announcements) {
		super();
		this.id = id;
		this.courseCode = courseCode;
		this.syllabus = syllabus;
		this.name = name;
		this.ects = ects;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.teachingMaterials = teachingMaterials;
		this.examAttempts = examAttempts;
		this.teacher = teacher;
		this.students = students;
		this.announcements = announcements;
		this.studyProgram = studyProgram;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSyllabus() {
		return syllabus;
	}

	public void setSyllabus(String syllabus) {
		this.syllabus = syllabus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEcts() {
		return ects;
	}

	public void setEcts(int ects) {
		this.ects = ects;
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<TeachingMaterial> getTeachingMaterials() {
		return teachingMaterials;
	}

	public void setTeachingMaterials(Set<TeachingMaterial> teachingMaterials) {
		this.teachingMaterials = teachingMaterials;
	}

	public Set<ExamAttempt> getExamAttempts() {
		return examAttempts;
	}

	public void setExamAttempts(Set<ExamAttempt> examAttempts) {
		this.examAttempts = examAttempts;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	public Set<CourseAnnouncement> getAnnouncements() {
		return announcements;
	}

	public void setAnnouncements(Set<CourseAnnouncement> announcements) {
		this.announcements = announcements;
	}

	public StudyProgram getStudyProgram() {
		return studyProgram;
	}

	public void setStudyProgram(StudyProgram studyProgram) {
		this.studyProgram = studyProgram;
	}

}
