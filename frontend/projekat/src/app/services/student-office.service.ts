import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StudentOffice } from '../model/users/student-office';
import { CourseMaterial } from '../model/academic/course-material';

@Injectable({
  providedIn: 'root'
})
export class StudentOfficeService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<StudentOffice[]>(`${environment.apiUrl}/api/studentskaofficeStaff`);
  }

  getById(id: number){
    return this.http.get<StudentOffice>(`${environment.apiUrl}/api/studentskaofficeStaff/${id}`);
  }

  delete(id: number){
    return this.http.delete<StudentOffice>(`${environment.apiUrl}/api/studentskaofficeStaff/${id}`);
  }

  update(id: number, StudentOffice: StudentOffice){
    return this.http.put<StudentOffice>(`${environment.apiUrl}/api/studentskaofficeStaff/${id}`, StudentOffice);
  }

  create(StudentOffice: StudentOffice){
    return this.http.post<StudentOffice>(`${environment.apiUrl}/api/studentskaofficeStaff`, StudentOffice);
  }

  getusers(){
    return this.http.get<any[]>(`${environment.apiUrl}/api/registrovaniusers`);
  }

  getUdzbenici(){
    return this.http.get<CourseMaterial[]>(`${environment.apiUrl}/api/nastavniMaterijal`);
  }

  saveYearEnrollment(sng: any){
    return this.http.post<any>(`${environment.apiUrl}//api/sng`, sng)
  }

}

