import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CourseMaterial } from '../model/academic/course-material';

@Injectable({
  providedIn: 'root'
})
export class CourseMaterialService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<CourseMaterial[]>(`${environment.apiUrl}/api/nastavniMaterijal`);
  }

  getById(id: number){
    return this.http.get<CourseMaterial>(`http://localhost:8080/api/nastavniMaterijal/${id}`);
  }

  delete(id: number){
    return this.http.delete<CourseMaterial>(`http://localhost:8080/api/nastavniMaterijal/${id}`);
  }

  update(id: number, CourseMaterial: CourseMaterial){
    return this.http.put<CourseMaterial>(`http://localhost:8080/api/nastavniMaterijal/${id}`, CourseMaterial);
  }

  create(CourseMaterial: CourseMaterial){
    return this.http.post<CourseMaterial>(`${environment.apiUrl}/api/nastavniMaterijal`, CourseMaterial);
  }
}
