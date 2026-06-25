import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Teacher } from '../model/users/nastavnik';
import { Course } from '../model/academic/predmet';
import { Notification } from '../model/obavestenje';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<Teacher[]>('http://localhost:8080/api/teachers');
  }

  getById(id: number) {
    return this.http.get<Teacher>(`http://localhost:8080/api/teachers/${id}`);
  }

  delete(id: number) {
    return this.http.delete<Teacher>(`http://localhost:8080/api/teachers/${id}`);
  }

  update(id: number, teacher: Teacher) {
    return this.http.put<Teacher>(`http://localhost:8080/api/teachers/${id}`, teacher);
  }

  create(teacher: Teacher) {
    return this.http.post<Teacher>('http://localhost:8080/api/teachers', teacher);
  }

  getMyCourses(id: number) {
    return this.http.get<any>(`http://localhost:8080/api/teachers/${id}/my-courses`);
  }

  updateSyllabus(id: number, course: Course) {
    return this.http.put<any>(`http://localhost:8080/api/courses/${id}/syllabus`, course);
  }

  getCourseNotifications(id: number) {
    return this.http.get<Notification[]>(`http://localhost:8080/api/course-announcements/${id}`);
  }

  getStudentInfo(id: number) {
    return this.http.get<any>(`http://localhost:8080/api/students/${id}`);
  }
}
