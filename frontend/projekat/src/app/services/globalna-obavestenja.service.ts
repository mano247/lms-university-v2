import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GObavestenje } from '../model/gObavestenje';

@Injectable({
  providedIn: 'root'
})
export class GlobalnaObavestenjaService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<GObavestenje[]>(`${environment.apiUrl}/api/obavestenja`);
  }

  getById(id: number){
    return this.http.get<GObavestenje>(`http://localhost:8080/api/obavestenja/${id}`);
  }

  delete(id: number){
    return this.http.delete<GObavestenje>(`http://localhost:8080/api/obavestenja/${id}`);
  }

  update(id: number, obavestenje: GObavestenje){
    return this.http.put<GObavestenje>(`http://localhost:8080/api/obavestenja/${id}`, obavestenje);
  }

  create(obavestenje: GObavestenje){
    return this.http.post<GObavestenje>(`${environment.apiUrl}/api/obavestenja`, obavestenje);
  }
}
