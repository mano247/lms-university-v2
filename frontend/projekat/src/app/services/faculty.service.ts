import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Faculty } from '../model/academic/faculty';

@Injectable({
  providedIn: 'root'
})
export class FacultyService {
  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Faculty[]>(`${environment.apiUrl}/api/faculties`);
  }

  getById(id: number){
    return this.http.get<Faculty>(`${environment.apiUrl}/api/faculties/${id}`);
  }

  getBySifra(sifra: string){
    return this.http.get<Faculty>(`${environment.apiUrl}/api/faculties/s/${sifra}`);
  }

  delete(id: number){
    return this.http.delete<Faculty>(`${environment.apiUrl}/api/faculties/${id}`);
  }

  update(id: number, faculty: Faculty){
    return this.http.put<Faculty>(`${environment.apiUrl}/api/faculties/${id}`, faculty);
  }

  create(faculty: Faculty){
    return this.http.post<Faculty>(`${environment.apiUrl}/api/faculties`, faculty);
  }
}
