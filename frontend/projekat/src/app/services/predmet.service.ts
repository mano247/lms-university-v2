import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Predmet } from '../model/academic/predmet';

@Injectable({
  providedIn: 'root'
})
export class PredmetService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Predmet[]>(`${environment.apiUrl}/api/predmeti`);
  }

  getById(id: number){
    return this.http.get<Predmet>(`http://localhost:8080/api/predmeti/${id}`);
  }

  getBySifra(sifra: string){
    return this.http.get<Predmet>(`http://localhost:8080/api/predmeti/s/${sifra}`);
  }

  delete(id: number){
    return this.http.delete<Predmet>(`http://localhost:8080/api/predmeti/${id}`);
  }

  update(id: number, predmet: Predmet){
    return this.http.put<Predmet>(`http://localhost:8080/api/predmeti/${id}`, predmet);
  }

  create(predmet: Predmet){
    return this.http.post<Predmet>(`${environment.apiUrl}/api/predmeti`, predmet);
  }
}
