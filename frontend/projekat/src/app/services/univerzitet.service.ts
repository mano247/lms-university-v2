import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Univerzitet } from '../model/academic/univerzitet';

@Injectable({
  providedIn: 'root'
})
export class UniverzitetService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Univerzitet[]>(`${environment.apiUrl}/api/univerziteti`);
  }

  getById(id: number){
    return this.http.get<Univerzitet>(`http://localhost:8080/api/univerziteti/${id}`);
  }

  delete(id: number){
    return this.http.delete<Univerzitet>(`http://localhost:8080/api/univerziteti/${id}`);
  }

  update(id: number, univerzitet: Univerzitet){
    return this.http.put<Univerzitet>(`http://localhost:8080/api/univerziteti/${id}`, univerzitet);
  }

  create(univerzitet: Univerzitet){
    return this.http.post<Univerzitet>(`${environment.apiUrl}/api/univerziteti`, univerzitet);
  }
}
