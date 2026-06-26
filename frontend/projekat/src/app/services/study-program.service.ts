import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StudyProgram } from '../model/academic/study-program';

@Injectable({
  providedIn: 'root'
})
export class StudyProgramService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<StudyProgram[]>(`${environment.apiUrl}/api/studijskiProgrami`);
  }

  getById(id: number){
    return this.http.get<StudyProgram>(`${environment.apiUrl}/api/studijskiProgrami/${id}`);
  }

  getBySifra(sifraSP: any){
    return this.http.get<StudyProgram>(`${environment.apiUrl}/api/studijskiProgrami/s/${sifraSP}`);
  }

  delete(id: number){
    return this.http.delete<StudyProgram>(`${environment.apiUrl}/api/studijskiProgrami/${id}`);
  }

  update(id: number, studyProgram: StudyProgram){
    return this.http.put<StudyProgram>(`${environment.apiUrl}/api/studijskiProgrami/${id}`, studyProgram);
  }

  create(studyProgram: StudyProgram){
    return this.http.post<StudyProgram>(`${environment.apiUrl}/api/studijskiProgrami`, studyProgram);
  }
}
