import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NastavniMaterijal } from '../model/academic/nastavniMaterijal';

@Injectable({
  providedIn: 'root'
})
export class NastavniMaterijalService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<NastavniMaterijal[]>(`${environment.apiUrl}/api/nastavnimaterijal`);
  }

  getById(id: number){
    return this.http.get<NastavniMaterijal>(`http://localhost:8080/api/nastavnimaterijal/${id}`);
  }

  delete(id: number){
    return this.http.delete<NastavniMaterijal>(`http://localhost:8080/api/nastavnimaterijal/${id}`);
  }

  update(id: number, nastavniMaterijal: NastavniMaterijal){
    return this.http.put<NastavniMaterijal>(`http://localhost:8080/api/nastavnimaterijal/${id}`, nastavniMaterijal);
  }

  create(nastavniMaterijal: NastavniMaterijal){
    return this.http.post<NastavniMaterijal>(`${environment.apiUrl}/api/nastavnimaterijal`, nastavniMaterijal);
  }
}
