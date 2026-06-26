import { environment } from '../../environments/environment';
import { Injectable } from '@angular/core';
import { Administrator } from '../model/users/administrator';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AdministratorService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Administrator[]>(`${environment.apiUrl}/api/admins`);
  }

  getById(id: number){
    return this.http.get<Administrator>(`http://localhost:8080/api/admins/${id}`);
  }

  delete(id: number){
    return this.http.delete<Administrator>(`http://localhost:8080/api/admins/${id}`);
  }

  update(id: number, administrator: Administrator){
    return this.http.put<Administrator>(`http://localhost:8080/api/admins/${id}`, administrator);
  }

  create(administrator: Administrator){
    return this.http.post<Administrator>(`${environment.apiUrl}/api/admins`, administrator);
  }

  dodelaStatusa( tip: string, student: any){
    return this.http.put<any>(`http://localhost:8080/api/registrovaniusers/izmeniTip/${tip}`, student);
  }
}

