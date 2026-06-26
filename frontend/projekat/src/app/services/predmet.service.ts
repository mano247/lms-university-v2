import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Course } from '../model/academic/predmet';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<Course[]>('http://localhost:8080/api/courses');
  }

  getById(id: number) {
    return this.http.get<Course>(`http://localhost:8080/api/courses/${id}`);
  }

  getByCode(code: string) {
    return this.http.get<Course>(`http://localhost:8080/api/courses/code/${code}`);
  }

  delete(id: number) {
    return this.http.delete<Course>(`http://localhost:8080/api/courses/${id}`);
  }

  update(id: number, course: Course) {
    return this.http.put<Course>(`http://localhost:8080/api/courses/${id}`, course);
  }

  create(course: Course) {
    return this.http.post<Course>('http://localhost:8080/api/courses', course);
  }
}
