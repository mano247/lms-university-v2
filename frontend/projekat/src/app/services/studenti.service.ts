import { Injectable } from '@angular/core';
import { Student } from '../model/users/student';
import { HttpClient } from '@angular/common/http';
import { Course } from '../model/academic/predmet';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<Student[]>('http://localhost:8080/api/students');
  }

  getById(id: number) {
    return this.http.get<Student>(`http://localhost:8080/api/students/${id}`);
  }

  delete(id: number) {
    return this.http.delete<Student>(`http://localhost:8080/api/students/${id}`);
  }

  update(id: number, student: Student) {
    return this.http.put<Student>(`http://localhost:8080/api/students/${id}`, student);
  }

  create(student: Student) {
    return this.http.post<Student>('http://localhost:8080/api/students', student);
  }

  getAllCourses(id: number) {
    return this.http.get<Course[]>(`http://localhost:8080/api/students/${id}/all-courses`);
  }

  getPassedExams(id: number) {
    return this.http.get<any[]>(`http://localhost:8080/api/students/${id}/passed-exams`);
  }

  getFailedExams(id: number) {
    return this.http.get<Course[]>(`http://localhost:8080/api/students/${id}/failed-exams`);
  }

  getActiveCourses(id: number) {
    return this.http.get<Course[]>(`http://localhost:8080/api/students/${id}/active-courses`);
  }

  getAvailableExams(id: number) {
    return this.http.get<Course[]>(`http://localhost:8080/api/students/${id}/available-exams`);
  }

  getEnrollments(id: number) {
    return this.http.get<any[]>(`http://localhost:8080/api/enrollments/by-student/${id}`);
  }

  enrollInYear(enrollment: any) {
    return this.http.post<any>('http://localhost:8080/api/enrollments', enrollment);
  }

  addStudentToCourse(programId: number, student: any) {
    return this.http.put<any>(`http://localhost:8080/api/students/assign-courses/${programId}`, student);
  }
}
