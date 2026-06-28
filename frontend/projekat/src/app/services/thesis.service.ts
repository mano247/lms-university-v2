import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ThesisService {
  constructor(private http: HttpClient) { }

  findByStudent(id: number) {
    return this.http.get<any>(`${environment.apiUrl}/api/final-thesis/by-student/${id}`);
  }
}
