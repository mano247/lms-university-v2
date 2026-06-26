import { environment } from '../../environments/environment';
import { Injectable } from '@angular/core';
import { Student } from '../model/users/student';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Course } from '../model/academic/course';
import { Notification } from '../model/notification';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Student[]>(`${environment.apiUrl}/api/studenti`);
  }

  getById(id: number){
    return this.http.get<Student>(`http://localhost:8080/api/studenti/${id}`);
  }

  delete(id: number){
    return this.http.delete<Student>(`http://localhost:8080/api/studenti/${id}`);
  }

  update(id: number, student: Student){
    return this.http.put<Student>(`http://localhost:8080/api/studenti/${id}`, student);
  }

  create(student: Student){
    return this.http.post<Student>(`${environment.apiUrl}/api/studenti`, student);
  }


  sviIspiti(id: number){
    return this.http.get<Course[]>(`http://localhost:8080/api/studenti/${id}/sviIspiti`);
  }

  polozeniIspiti(id: number){
    return this.http.get<any[]>(`http://localhost:8080/api/studenti/${id}/polozeniIspiti`);
  }

  nepolozeniIspiti(id: number){
    return this.http.get<Course[]>(`http://localhost:8080/api/studenti/${id}/nepolozeniIspiti`);
  }

  getAktivnicoursei(id: number){
    return this.http.get<Course[]>(`http://localhost:8080/api/studenti/${id}/sviAcoursei`)
  }

  getIspitiZaPrijavu(id: number){
    return this.http.get<Course[]>(`http://localhost:8080/api/studenti/${id}/ispitiZaPrijavu`);
  }

  getenrollments(id: number){
    return this.http.get<any[]>(`http://localhost:8080/api/sng/fbs/${id}`);
  }

  saveYearEnrollment(sng: any){
    return this.http.post<any>(`${environment.apiUrl}/api/sng`, sng);
  }

  dodajStudentaNacourse(idSmer: number, student: any){
    return this.http.put<any>(`http://localhost:8080/api/studenti/dsp/${idSmer}`, student);
  } 

}


