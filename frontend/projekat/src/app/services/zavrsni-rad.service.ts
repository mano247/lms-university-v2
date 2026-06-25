import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ZavrsniRadService {

  constructor(private http: HttpClient) { }

  findByStudent(id: number){
    return this.http.get<any>(`http://localhost:8080/api/zavrsniRad/fbs/${id}`);
  }
}
