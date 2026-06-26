import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisteredUser } from '../model/users/registered-user';

@Injectable({
  providedIn: 'root'
})
export class RegisteredUserService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<RegisteredUser[]>(`${environment.apiUrl}/api/registrovaniusers`);
  }

  getById(id: number){
    return this.http.get<RegisteredUser>(`${environment.apiUrl}/api/registrovaniusers/${id}`);
  }

  delete(id: number){
    return this.http.delete<RegisteredUser>(`${environment.apiUrl}/api/registrovaniusers/${id}`);
  }

  update(id: number, user: RegisteredUser){
    return this.http.put<RegisteredUser>(`${environment.apiUrl}/api/registrovaniusers/${id}`, user);
  }

  create(user: RegisteredUser){
    return this.http.post<RegisteredUser>(`${environment.apiUrl}/api/registrovaniusers`, user);
  }

  dodeliStudenta(id: number, student: any){
    return this.http.put<any>(`${environment.apiUrl}/api/registrovaniusers/${id}/dodeliStudenta`, student);
  }
}
