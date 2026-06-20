package com.lmsuniversity.common;

import jakarta.persistence.Column;

import java.util.List;

public class AddGradeDto {

    private Long courseId;
    private String courseName;
    private int ects;
    @Column(columnDefinition = "LONGTEXT")
    private String syllabus;

    private List<TeacherStudentsDto> studentsInCourse;


    public AddGradeDto(Long courseId, String courseName, int ects, String syllabus, List<TeacherStudentsDto> studentsInCourse) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.ects = ects;
        this.syllabus = syllabus;
        this.studentsInCourse = studentsInCourse;

    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public List<TeacherStudentsDto> getStudentsInCourse() {
        return studentsInCourse;
    }

    public void setStudentsInCourse(List<TeacherStudentsDto> studentsInCourse) {
        this.studentsInCourse = studentsInCourse;
    }
}
