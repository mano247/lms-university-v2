import { environment } from '../../environments/environment';
import { Injectable } from '@angular/core';
import { Administrator } from '../model/users/administrator';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdministratorService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any>(`${environment.apiUrl}/api/administrators?size=10000`).pipe(
      map((r: any): Administrator[] => Array.isArray(r) ? r : (r?.content ?? []))
    );
  }

  getById(id: number) {
    return this.http.get<Administrator>(`${environment.apiUrl}/api/administrators/${id}`);
  }

  delete(id: number) {
    return this.http.delete<Administrator>(`${environment.apiUrl}/api/administrators/${id}`);
  }

  update(id: number, administrator: Administrator) {
    return this.http.put<Administrator>(`${environment.apiUrl}/api/administrators/${id}`, administrator);
  }

  create(administrator: Administrator) {
    return this.http.post<Administrator>(`${environment.apiUrl}/api/administrators`, administrator);
  }

  assignStatus(type: string, user: any) {
    return this.http.put<any>(`${environment.apiUrl}/api/registered-users/change-type/${type}`, user);
  }
}
