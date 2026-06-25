import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Polaganje } from '../model/polaganje';

@Injectable({
  providedIn: 'root'
})
export class PolaganjeService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Polaganje[]>(`${environment.apiUrl}/api/polaganja`);
  }

  getById(id: number){
    return this.http.get<Polaganje>(`http://localhost:8080/api/polaganja/${id}`);
  }

  delete(id: number){
    return this.http.delete<Polaganje>(`http://localhost:8080/api/polaganja/${id}`);
  }

  update(id: number, polaganje: Polaganje){
    return this.http.put<Polaganje>(`http://localhost:8080/api/polaganja/${id}`, polaganje);
  }

  create(polaganje: Polaganje){
    return this.http.post<Polaganje>(`${environment.apiUrl}/api/polaganja/c`, polaganje);
  }

  getPrijavljeni(id: number){
    return this.http.get<any[]>(`http://localhost:8080/api/polaganja/prijavljeni/${id}`);
  }

  getPrijavljeniPoPredmetu(id: number){
    return this.http.get<any[]>(`http://localhost:8080/api/polaganja/prijavljeniPoPredmetu/${id}`)
  }
}
