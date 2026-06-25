import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ExamAttempt } from '../model/polaganje';

@Injectable({
  providedIn: 'root'
})
export class ExamAttemptService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<ExamAttempt[]>('http://localhost:8080/api/examAttempts');
  }

  getById(id: number) {
    return this.http.get<ExamAttempt>(`http://localhost:8080/api/examAttempts/${id}`);
  }

  delete(id: number) {
    return this.http.delete<ExamAttempt>(`http://localhost:8080/api/examAttempts/${id}`);
  }

  update(id: number, examAttempt: ExamAttempt) {
    return this.http.put<ExamAttempt>(`http://localhost:8080/api/examAttempts/${id}`, examAttempt);
  }

  create(examAttempt: ExamAttempt) {
    return this.http.post<ExamAttempt>('http://localhost:8080/api/examAttempts/register', examAttempt);
  }

  getRegisteredByStudent(id: number) {
    return this.http.get<any[]>(`http://localhost:8080/api/examAttempts/registered/${id}`);
  }

  getRegisteredByCourse(id: number) {
    return this.http.get<any[]>(`http://localhost:8080/api/examAttempts/registered-by-course/${id}`);
  }
}
