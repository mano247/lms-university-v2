import { environment } from '../../environments/environment';
import { Injectable } from '@angular/core';
import { Student } from '../model/users/student';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs';
import { Course } from '../model/academic/course';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any>(`${environment.apiUrl}/api/students?size=10000`).pipe(
      map((r: any): Student[] => Array.isArray(r) ? r : (r?.content ?? []))
    );
  }

  getById(id: number) {
    return this.http.get<Student>(`${environment.apiUrl}/api/students/${id}`);
  }

  delete(id: number) {
    return this.http.delete<Student>(`${environment.apiUrl}/api/students/${id}`);
  }

  update(id: number, student: Student) {
    return this.http.put<Student>(`${environment.apiUrl}/api/students/${id}`, student);
  }

  create(student: Student) {
    return this.http.post<Student>(`${environment.apiUrl}/api/students`, student);
  }

  getAllCourses(id: number) {
    return this.http.get<Course[]>(`${environment.apiUrl}/api/students/${id}/all-courses`);
  }

  getPassedExams(id: number) {
    return this.http.get<any[]>(`${environment.apiUrl}/api/students/${id}/passed-exams`);
  }

  getFailedExams(id: number) {
    return this.http.get<Course[]>(`${environment.apiUrl}/api/students/${id}/failed-exams`);
  }

  getActiveCourses(id: number) {
    return this.http.get<Course[]>(`${environment.apiUrl}/api/students/${id}/active-courses`);
  }

  getAvailableExams(id: number) {
    return this.http.get<Course[]>(`${environment.apiUrl}/api/students/${id}/available-exams`);
  }

  getEnrollments(id: number) {
    return this.http.get<any[]>(`${environment.apiUrl}/api/enrollments/by-student/${id}`);
  }

  enrollInYear(enrollment: any) {
    return this.http.post<any>(`${environment.apiUrl}/api/enrollments`, enrollment);
  }

  addStudentToCourse(programId: number, student: any) {
    return this.http.put<any>(`${environment.apiUrl}/api/students/assign-courses/${programId}`, student);
  }
}
