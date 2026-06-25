import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StudijskiProgram } from '../model/academic/studijskiProgram';

@Injectable({
  providedIn: 'root'
})
export class StudijskiProgramService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<StudijskiProgram[]>(`${environment.apiUrl}/api/studijskiProgrami`);
  }

  getById(id: number){
    return this.http.get<StudijskiProgram>(`http://localhost:8080/api/studijskiProgrami/${id}`);
  }

  getBySifra(sifraSP: any){
    return this.http.get<StudijskiProgram>(`http://localhost:8080/api/studijskiProgrami/s/${sifraSP}`);
  }

  delete(id: number){
    return this.http.delete<StudijskiProgram>(`http://localhost:8080/api/studijskiProgrami/${id}`);
  }

  update(id: number, studijskiProgram: StudijskiProgram){
    return this.http.put<StudijskiProgram>(`http://localhost:8080/api/studijskiProgrami/${id}`, studijskiProgram);
  }

  create(studijskiProgram: StudijskiProgram){
    return this.http.post<StudijskiProgram>(`${environment.apiUrl}/api/studijskiProgrami`, studijskiProgram);
  }
}
