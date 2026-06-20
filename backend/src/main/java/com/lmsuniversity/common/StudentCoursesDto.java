package com.lmsuniversity.common;

public class StudentCoursesDto {

    private String courseName;
    private int ects;
    private Double points;
    private int grade;

    public StudentCoursesDto(String courseName, int ects, Double points, int grade) {
        this.courseName = courseName;
        this.ects = ects;
        this.points = points;
        this.grade = grade;
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
