import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisteredUser } from '../model/users/registrovaniKorisnik';

@Injectable({
  providedIn: 'root'
})
export class RegisteredUserService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<RegisteredUser[]>('http://localhost:8080/api/registered-users');
  }

  getById(id: number) {
    return this.http.get<RegisteredUser>(`http://localhost:8080/api/registered-users/${id}`);
  }

  delete(id: number) {
    return this.http.delete<RegisteredUser>(`http://localhost:8080/api/registered-users/${id}`);
  }

  update(id: number, user: RegisteredUser) {
    return this.http.put<RegisteredUser>(`http://localhost:8080/api/registered-users/${id}`, user);
  }

  create(user: RegisteredUser) {
    return this.http.post<RegisteredUser>('http://localhost:8080/api/registered-users', user);
  }

  assignStudent(id: number, student: any) {
    return this.http.put<any>(`http://localhost:8080/api/registered-users/${id}/enroll-student`, student);
  }
}
