import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Teacher } from '../model/users/teacher';
import { Course } from '../model/academic/course';
import { Notification } from '../model/announcement';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<Teacher[]>(`${environment.apiUrl}/api/teachers`);
  }

  getById(id: number) {
    return this.http.get<Teacher>(`${environment.apiUrl}/api/teachers/${id}`);
  }

  delete(id: number) {
    return this.http.delete<Teacher>(`${environment.apiUrl}/api/teachers/${id}`);
  }

  update(id: number, teacher: Teacher) {
    return this.http.put<Teacher>(`${environment.apiUrl}/api/teachers/${id}`, teacher);
  }

  create(teacher: Teacher) {
    return this.http.post<Teacher>(`${environment.apiUrl}/api/teachers`, teacher);
  }

  getMyCourses(id: number) {
    return this.http.get<any>(`${environment.apiUrl}/api/teachers/${id}/my-courses`);
  }

  updateSyllabus(id: number, course: Course) {
    return this.http.put<any>(`${environment.apiUrl}/api/courses/${id}/syllabus`, course);
  }

  getCourseNotifications(id: number) {
    return this.http.get<Notification[]>(`${environment.apiUrl}/api/course-announcements/${id}`);
  }

  getStudentInfo(id: number) {
    return this.http.get<any>(`${environment.apiUrl}/api/students/${id}`);
  }
}
