import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { University } from '../model/academic/university';

@Injectable({
  providedIn: 'root'
})
export class UniversityService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<University[]>(`${environment.apiUrl}/api/univerziteti`);
  }

  getById(id: number){
    return this.http.get<University>(`${environment.apiUrl}/api/univerziteti/${id}`);
  }

  delete(id: number){
    return this.http.delete<University>(`${environment.apiUrl}/api/univerziteti/${id}`);
  }

  update(id: number, university: University){
    return this.http.put<University>(`${environment.apiUrl}/api/univerziteti/${id}`, university);
  }

  create(university: University){
    return this.http.post<University>(`${environment.apiUrl}/api/univerziteti`, university);
  }
}
