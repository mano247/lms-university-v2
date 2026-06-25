import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { KancelarijskiMaterijal } from '../model/kancelarijskiMaterijal';

@Injectable({
  providedIn: 'root'
})
export class KancelarijskiMaterijalService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<KancelarijskiMaterijal[]>(`${environment.apiUrl}/api/kancelariskiMaterial`);
  }

  getById(id: number){
    return this.http.get<KancelarijskiMaterijal>(`http://localhost:8080/api/kancelariskiMaterial/${id}`);
  }

  delete(id: number){
    return this.http.delete<KancelarijskiMaterijal>(`http://localhost:8080/api/kancelariskiMaterial/${id}`);
  }

  update(id: number, kMaterijal: KancelarijskiMaterijal){
    return this.http.put<KancelarijskiMaterijal>(`http://localhost:8080/api/kancelariskiMaterial/${id}`, kMaterijal);
  }

  create(kMaterijal: KancelarijskiMaterijal){
    return this.http.post<KancelarijskiMaterijal>(`${environment.apiUrl}/api/kancelariskiMaterial`, kMaterijal);
  }
}
