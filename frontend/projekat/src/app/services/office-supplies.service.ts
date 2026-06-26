import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OfficeMaterial } from '../model/office-material';

@Injectable({
  providedIn: 'root'
})
export class OfficeMaterialService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<OfficeMaterial[]>('http://localhost:8080/api/office-supplies');
  }

  getById(id: number) {
    return this.http.get<OfficeMaterial>(`http://localhost:8080/api/office-supplies/${id}`);
  }

  delete(id: number) {
    return this.http.delete<OfficeMaterial>(`http://localhost:8080/api/office-supplies/${id}`);
  }

  update(id: number, material: OfficeMaterial) {
    return this.http.put<OfficeMaterial>(`http://localhost:8080/api/office-supplies/${id}`, material);
  }

  create(material: OfficeMaterial) {
    return this.http.post<OfficeMaterial>('http://localhost:8080/api/office-supplies', material);
  }
}
