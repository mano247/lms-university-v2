import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrovaniKorisnik } from '../model/users/registrovaniKorisnik';

@Injectable({
  providedIn: 'root'
})
export class RegistrovaniKorisnikService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<RegistrovaniKorisnik[]>(`${environment.apiUrl}/api/registrovaniKorisnici`);
  }

  getById(id: number){
    return this.http.get<RegistrovaniKorisnik>(`http://localhost:8080/api/registrovaniKorisnici/${id}`);
  }

  delete(id: number){
    return this.http.delete<RegistrovaniKorisnik>(`http://localhost:8080/api/registrovaniKorisnici/${id}`);
  }

  update(id: number, registrovaniKorisnik: RegistrovaniKorisnik){
    return this.http.put<RegistrovaniKorisnik>(`http://localhost:8080/api/registrovaniKorisnici/${id}`, registrovaniKorisnik);
  }

  create(registrovaniKorisnik: RegistrovaniKorisnik){
    return this.http.post<RegistrovaniKorisnik>(`${environment.apiUrl}/api/registrovaniKorisnici`, registrovaniKorisnik);
  }

  dodeliStudenta(id: number, student: any){
    return this.http.put<any>(`http://localhost:8080/api/registrovaniKorisnici/${id}/dodeliStudenta`, student);
  }
}
