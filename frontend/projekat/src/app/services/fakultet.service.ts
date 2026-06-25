import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Faculty } from '../model/academic/fakultet';

@Injectable({
  providedIn: 'root'
})
export class FacultyService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<Faculty[]>('http://localhost:8080/api/faculties');
  }

  getById(id: number) {
    return this.http.get<Faculty>(`http://localhost:8080/api/faculties/${id}`);
  }

  getByCode(code: string) {
    return this.http.get<Faculty>(`http://localhost:8080/api/faculties/code/${code}`);
  }

  delete(id: number) {
    return this.http.delete<Faculty>(`http://localhost:8080/api/faculties/${id}`);
  }

  update(id: number, faculty: Faculty) {
    return this.http.put<Faculty>(`http://localhost:8080/api/faculties/${id}`, faculty);
  }

  create(faculty: Faculty) {
    return this.http.post<Faculty>('http://localhost:8080/api/faculties', faculty);
  }
}
