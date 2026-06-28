import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StudyProgram } from '../model/academic/study-program';

@Injectable({
  providedIn: 'root'
})
export class StudyProgramService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<StudyProgram[]>(`${environment.apiUrl}/api/studyPrograms`);
  }

  getById(id: number) {
    return this.http.get<StudyProgram>(`${environment.apiUrl}/api/studyPrograms/${id}`);
  }

  getByCode(code: any) {
    return this.http.get<StudyProgram>(`${environment.apiUrl}/api/studyPrograms/code/${code}`);
  }

  delete(id: number) {
    return this.http.delete<StudyProgram>(`${environment.apiUrl}/api/studyPrograms/${id}`);
  }

  update(id: number, studyProgram: StudyProgram) {
    return this.http.put<StudyProgram>(`${environment.apiUrl}/api/studyPrograms/${id}`, studyProgram);
  }

  create(studyProgram: StudyProgram) {
    return this.http.post<StudyProgram>(`${environment.apiUrl}/api/studyPrograms`, studyProgram);
  }
}
