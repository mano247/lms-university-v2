import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CourseMaterial } from '../model/academic/teaching-material';

@Injectable({
  providedIn: 'root'
})
export class CourseMaterialService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<CourseMaterial[]>(`${environment.apiUrl}/api/teaching-materials`);
  }

  getById(id: number) {
    return this.http.get<CourseMaterial>(`${environment.apiUrl}/api/teaching-materials/${id}`);
  }

  delete(id: number) {
    return this.http.delete<CourseMaterial>(`${environment.apiUrl}/api/teaching-materials/${id}`);
  }

  update(id: number, material: CourseMaterial) {
    return this.http.put<CourseMaterial>(`${environment.apiUrl}/api/teaching-materials/${id}`, material);
  }

  create(material: CourseMaterial) {
    return this.http.post<CourseMaterial>(`${environment.apiUrl}/api/teaching-materials`, material);
  }
}
