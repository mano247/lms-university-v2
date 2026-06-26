import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Course } from '../model/academic/course';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Course[]>(`${environment.apiUrl}/api/predmeti`);
  }

  getById(id: number){
    return this.http.get<Course>(`${environment.apiUrl}/api/predmeti/${id}`);
  }

  getBySifra(sifra: string){
    return this.http.get<Course>(`${environment.apiUrl}/api/predmeti/s/${sifra}`);
  }

  delete(id: number){
    return this.http.delete<Course>(`${environment.apiUrl}/api/predmeti/${id}`);
  }

  update(id: number, course: Course){
    return this.http.put<Course>(`${environment.apiUrl}/api/predmeti/${id}`, course);
  }

  create(course: Course){
    return this.http.post<Course>(`${environment.apiUrl}/api/predmeti`, course);
  }
}
