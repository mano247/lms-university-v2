import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GObavestenje } from '../model/gObavestenje';
import { Obavestenje } from '../model/obavestenje';

@Injectable({
  providedIn: 'root'
})
export class ObavestenjeService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Obavestenje[]>(`${environment.apiUrl}/api/predmetnaObavestenja`);
  }

  getById(id: number){
    return this.http.get<Obavestenje>(`http://localhost:8080/api/predmetnaObavestenja/${id}`);
  }

  delete(id: number){
    return this.http.delete<Obavestenje>(`http://localhost:8080/api/predmetnaObavestenja/${id}`);
  }

  update(id: number, obavestenje: Obavestenje){
    return this.http.put<Obavestenje>(`http://localhost:8080/api/predmetnaObavestenja/${id}`, obavestenje);
  }

  create(obavestenje: Obavestenje){
    return this.http.post<Obavestenje>(`${environment.apiUrl}/api/predmetnaObavestenja`, obavestenje);
  }

  getByPredmet(id: number){
    return this.http.get<Obavestenje[]>(`http://localhost:8080/api/predmetnaObavestenja/gbp/${id}`)
  }
}
