import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs';
import { OfficeMaterial } from '../model/office-material';

@Injectable({
  providedIn: 'root'
})
export class OfficeMaterialService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any>(`${environment.apiUrl}/api/office-supplies?size=10000`).pipe(
      map((r: any): OfficeMaterial[] => Array.isArray(r) ? r : (r?.content ?? []))
    );
  }

  getById(id: number) {
    return this.http.get<OfficeMaterial>(`${environment.apiUrl}/api/office-supplies/${id}`);
  }

  delete(id: number) {
    return this.http.delete<OfficeMaterial>(`${environment.apiUrl}/api/office-supplies/${id}`);
  }

  update(id: number, material: OfficeMaterial) {
    return this.http.put<OfficeMaterial>(`${environment.apiUrl}/api/office-supplies/${id}`, material);
  }

  create(material: OfficeMaterial) {
    return this.http.post<OfficeMaterial>(`${environment.apiUrl}/api/office-supplies`, material);
  }
}
