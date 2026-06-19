package rs.ac.singidunum.novisad.backend.dto;

public class TeacherStudentsDTO {

    private Long studentId;
    private String studentFirstName;
    private String studentLastName;
    private String indexNumber;

    private Long examAttemptId;
    private Double points;
    private int grade;

    public TeacherStudentsDTO(Long studentId, Long examAttemptId, String studentFirstName, String studentLastName, String indexNumber, Double points, int grade) {
        this.studentId = studentId;
        this.examAttemptId = examAttemptId;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.indexNumber = indexNumber;
        this.points = points;
        this.grade = grade;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getExamAttemptId() {
        return examAttemptId;
    }

    public void setExamAttemptId(Long examAttemptId) {
        this.examAttemptId = examAttemptId;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
