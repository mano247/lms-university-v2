package rs.ac.singidunum.novisad.backend.dto;

public class MyCoursesDTO {
    private Long id;

    private String courseName;
    private String description;
    private String syllabus;
    private TeacherDTO teacher;
    private Double points;
    private int ects;
    private int grade;

    public MyCoursesDTO(Long id, String courseName, String description, String syllabus, TeacherDTO teacher, Double points, int ects, int grade) {
        this.id = id;
        this.courseName = courseName;
        this.description = description;
        this.syllabus = syllabus;
        this.teacher = teacher;
        this.points = points;
        this.ects = ects;
        this.grade = grade;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public TeacherDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDTO teacher) {
        this.teacher = teacher;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
