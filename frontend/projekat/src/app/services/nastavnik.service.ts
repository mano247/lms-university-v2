import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Nastavnik } from '../model/users/nastavnik';
import { Student } from '../model/users/student';
import { Predmet } from '../model/academic/predmet';
import { Obavestenje } from '../model/obavestenje';

@Injectable({
  providedIn: 'root'
})
export class NastavnikService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Nastavnik[]>(`${environment.apiUrl}/api/nastavnici`);
  }

  getById(id: number){
    return this.http.get<Nastavnik>(`http://localhost:8080/api/nastavnici/${id}`);
  }

  delete(id: number){
    return this.http.delete<Nastavnik>(`http://localhost:8080/api/nastavnici/${id}`);
  }

  update(id: number, nastavnik: Nastavnik){
    return this.http.put<Nastavnik>(`http://localhost:8080/api/nastavnici/${id}`, nastavnik);
  }

  create(nastavnik: Nastavnik){
    return this.http.post<Nastavnik>(`${environment.apiUrl}/api/nastavnici`, nastavnik);
  }

  mojiPredmeti(id: number){
    return this.http.get<any>(`http://localhost:8080/api/nastavnici/${id}/mojiPredmeti`)
  }

  izmenaSilabusa(id: number, predmet: Predmet){
    return this.http.put<any>(`http://localhost:8080/api/predmeti/${id}/izmeniSilabus`, predmet)
  }

  getPO(id: number){
    return this.http.get<Obavestenje[]>(`http://localhost:8080/api/predmetnaObavestenja/${id}`)
  }

  getStudentInfo(id: number){
    return this.http.get<any>(`http://localhost:8080/api/studenti/${id}/podaci`)
  }
 
}
