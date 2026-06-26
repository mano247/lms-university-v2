import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StudentOffice } from '../model/users/student-affairs';
import { CourseMaterial } from '../model/academic/teaching-material';

@Injectable({
  providedIn: 'root'
})
export class StudentOfficeService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<StudentOffice[]>('http://localhost:8080/api/student-affairs-office');
  }

  getById(id: number) {
    return this.http.get<StudentOffice>(`http://localhost:8080/api/student-affairs-office/${id}`);
  }

  delete(id: number) {
    return this.http.delete<StudentOffice>(`http://localhost:8080/api/student-affairs-office/${id}`);
  }

  update(id: number, officeStaff: StudentOffice) {
    return this.http.put<StudentOffice>(`http://localhost:8080/api/student-affairs-office/${id}`, officeStaff);
  }

  create(officeStaff: StudentOffice) {
    return this.http.post<StudentOffice>('http://localhost:8080/api/student-affairs-office', officeStaff);
  }

  getUsers() {
    return this.http.get<any[]>('http://localhost:8080/api/registered-users');
  }

  getTextbooks() {
    return this.http.get<CourseMaterial[]>('http://localhost:8080/api/teaching-materials');
  }

  enrollInYear(enrollment: any) {
    return this.http.post<any>('http://localhost:8080/api/enrollments', enrollment);
  }
}
