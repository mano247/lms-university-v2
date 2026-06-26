import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OfficeSupply } from '../model/office-supply';

@Injectable({
  providedIn: 'root'
})
export class OfficeSuppliesService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<OfficeSupply[]>(`${environment.apiUrl}/api/kancelariskiMaterial`);
  }

  getById(id: number){
    return this.http.get<OfficeSupply>(`http://localhost:8080/api/kancelariskiMaterial/${id}`);
  }

  delete(id: number){
    return this.http.delete<OfficeSupply>(`http://localhost:8080/api/kancelariskiMaterial/${id}`);
  }

  update(id: number, kMaterijal: OfficeSupply){
    return this.http.put<OfficeSupply>(`http://localhost:8080/api/kancelariskiMaterial/${id}`, kMaterijal);
  }

  create(kMaterijal: OfficeSupply){
    return this.http.post<OfficeSupply>(`${environment.apiUrl}/api/kancelariskiMaterial`, kMaterijal);
  }
}
