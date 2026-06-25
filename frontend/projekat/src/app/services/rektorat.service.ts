import { environment } from '../../environments/environment';
import { Injectable } from '@angular/core';
import { Rektorat } from '../model/rektorat';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RektoratService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Rektorat[]>(`${environment.apiUrl}/api/rektorati`);
  }

  getById(id: number){
    return this.http.get<Rektorat>(`http://localhost:8080/api/rektorati/${id}`);
  }

  delete(id: number){
    return this.http.delete<Rektorat>(`http://localhost:8080/api/rektorati/${id}`);
  }

  update(id: number, rektorat: Rektorat){
    return this.http.put<Rektorat>(`http://localhost:8080/api/rektorati/${id}`, rektorat);
  }

  create(rektorat: Rektorat){
    return this.http.post<Rektorat>(`${environment.apiUrl}/api/rektorati`, rektorat);
  }
}
