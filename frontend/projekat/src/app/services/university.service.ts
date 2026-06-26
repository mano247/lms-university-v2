import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { University } from '../model/academic/university';

@Injectable({
  providedIn: 'root'
})
export class UniversityService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<University[]>('http://localhost:8080/api/universities');
  }

  getById(id: number) {
    return this.http.get<University>(`http://localhost:8080/api/universities/${id}`);
  }

  delete(id: number) {
    return this.http.delete<University>(`http://localhost:8080/api/universities/${id}`);
  }

  update(id: number, university: University) {
    return this.http.put<University>(`http://localhost:8080/api/universities/${id}`, university);
  }

  create(university: University) {
    return this.http.post<University>('http://localhost:8080/api/universities', university);
  }
}
