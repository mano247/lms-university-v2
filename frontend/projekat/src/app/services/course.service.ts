import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs';
import { Course } from '../model/academic/course';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any>(`${environment.apiUrl}/api/courses?size=10000`).pipe(
      map((r: any): Course[] => Array.isArray(r) ? r : (r?.content ?? []))
    );
  }

  getById(id: number) {
    return this.http.get<Course>(`${environment.apiUrl}/api/courses/${id}`);
  }

  getByCode(code: string) {
    return this.http.get<Course>(`${environment.apiUrl}/api/courses/code/${code}`);
  }

  delete(id: number) {
    return this.http.delete<Course>(`${environment.apiUrl}/api/courses/${id}`);
  }

  update(id: number, course: Course) {
    return this.http.put<Course>(`${environment.apiUrl}/api/courses/${id}`, course);
  }

  create(course: Course) {
    return this.http.post<Course>(`${environment.apiUrl}/api/courses`, course);
  }
}
