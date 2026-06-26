import { environment } from '../../environments/environment';
import { Injectable } from '@angular/core';
import { Rectorate } from '../model/rectorate';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RectorateService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<Rectorate[]>('http://localhost:8080/api/rectorates');
  }

  getById(id: number) {
    return this.http.get<Rectorate>(`http://localhost:8080/api/rectorates/${id}`);
  }

  delete(id: number) {
    return this.http.delete<Rectorate>(`http://localhost:8080/api/rectorates/${id}`);
  }

  update(id: number, rectorate: Rectorate) {
    return this.http.put<Rectorate>(`http://localhost:8080/api/rectorates/${id}`, rectorate);
  }

  create(rectorate: Rectorate) {
    return this.http.post<Rectorate>('http://localhost:8080/api/rectorates', rectorate);
  }
}
