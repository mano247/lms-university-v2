import { Injectable } from '@angular/core';
import { Administrator } from '../model/users/administrator';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AdministratorService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<Administrator[]>('http://localhost:8080/api/administrators');
  }

  getById(id: number) {
    return this.http.get<Administrator>(`http://localhost:8080/api/administrators/${id}`);
  }

  delete(id: number) {
    return this.http.delete<Administrator>(`http://localhost:8080/api/administrators/${id}`);
  }

  update(id: number, administrator: Administrator) {
    return this.http.put<Administrator>(`http://localhost:8080/api/administrators/${id}`, administrator);
  }

  create(administrator: Administrator) {
    return this.http.post<Administrator>('http://localhost:8080/api/administrators', administrator);
  }

  assignStatus(type: string, user: any) {
    return this.http.put<any>(`http://localhost:8080/api/registered-users/change-type/${type}`, user);
  }
}
