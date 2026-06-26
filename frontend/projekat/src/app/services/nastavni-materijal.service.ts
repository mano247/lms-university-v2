import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CourseMaterial } from '../model/academic/nastavniMaterijal';

@Injectable({
  providedIn: 'root'
})
export class CourseMaterialService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<CourseMaterial[]>('http://localhost:8080/api/teaching-materials');
  }

  getById(id: number) {
    return this.http.get<CourseMaterial>(`http://localhost:8080/api/teaching-materials/${id}`);
  }

  delete(id: number) {
    return this.http.delete<CourseMaterial>(`http://localhost:8080/api/teaching-materials/${id}`);
  }

  update(id: number, material: CourseMaterial) {
    return this.http.put<CourseMaterial>(`http://localhost:8080/api/teaching-materials/${id}`, material);
  }

  create(material: CourseMaterial) {
    return this.http.post<CourseMaterial>('http://localhost:8080/api/teaching-materials', material);
  }
}
