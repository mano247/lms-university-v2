import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Teacher } from '../model/users/teacher';
import { Course } from '../model/academic/course';
import { Notification } from '../model/notification';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Teacher[]>(`${environment.apiUrl}/api/nastavnici`);
  }

  getById(id: number){
    return this.http.get<Teacher>(`${environment.apiUrl}/api/nastavnici/${id}`);
  }

  delete(id: number){
    return this.http.delete<Teacher>(`${environment.apiUrl}/api/nastavnici/${id}`);
  }

  update(id: number, teacher: Teacher){
    return this.http.put<Teacher>(`${environment.apiUrl}/api/nastavnici/${id}`, teacher);
  }

  create(teacher: Teacher){
    return this.http.post<Teacher>(`${environment.apiUrl}/api/nastavnici`, teacher);
  }

  mojicoursei(id: number){
    return this.http.get<any>(`${environment.apiUrl}/api/nastavnici/${id}/mojicoursei`);
  }

  izmenaSilabusa(id: number, course: Course){
    return this.http.put<any>(`${environment.apiUrl}/api/predmeti/${id}/izmeniSilabus`, course);
  }

  getPO(id: number){
    return this.http.get<Notification[]>(`${environment.apiUrl}/api/predmetnaObavestenja/${id}`);
  }

  getStudentInfo(id: number){
    return this.http.get<any>(`${environment.apiUrl}/api/studenti/${id}/podaci`);
  }
}
