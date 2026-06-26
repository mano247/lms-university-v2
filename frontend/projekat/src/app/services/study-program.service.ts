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
    return this.http.get<StudyProgram[]>('http://localhost:8080/api/studyPrograms');
  }

  getById(id: number) {
    return this.http.get<StudyProgram>(`http://localhost:8080/api/studyPrograms/${id}`);
  }

  getByCode(code: any) {
    return this.http.get<StudyProgram>(`http://localhost:8080/api/studyPrograms/code/${code}`);
  }

  delete(id: number) {
    return this.http.delete<StudyProgram>(`http://localhost:8080/api/studyPrograms/${id}`);
  }

  update(id: number, studyProgram: StudyProgram) {
    return this.http.put<StudyProgram>(`http://localhost:8080/api/studyPrograms/${id}`, studyProgram);
  }

  create(studyProgram: StudyProgram) {
    return this.http.post<StudyProgram>('http://localhost:8080/api/studyPrograms', studyProgram);
  }
}
