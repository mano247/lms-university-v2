import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Fakultet } from '../model/academic/fakultet';

@Injectable({
  providedIn: 'root'
})
export class FakultetService {
  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Fakultet[]>(`${environment.apiUrl}/api/fakulteti`);
  }

  getById(id: number){
    return this.http.get<Fakultet>(`http://localhost:8080/api/fakulteti/${id}`);
  }

  getBySifra(sifra: string){
    return this.http.get<Fakultet>(`http://localhost:8080/api/fakulteti/s/${sifra}`);
  }

  delete(id: number){
    return this.http.delete<Fakultet>(`http://localhost:8080/api/fakulteti/${id}`);
  }

  update(id: number, fakultet: Fakultet){
    return this.http.put<Fakultet>(`http://localhost:8080/api/fakulteti/${id}`, fakultet);
  }

  create(fakultet: Fakultet){
    return this.http.post<Fakultet>(`${environment.apiUrl}/api/fakulteti`, fakultet);
  }
}
