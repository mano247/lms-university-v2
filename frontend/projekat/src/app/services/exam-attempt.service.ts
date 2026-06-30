import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs';
import { ExamAttempt } from '../model/exam-attempt';

@Injectable({
  providedIn: 'root'
})
export class ExamAttemptService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any>(`${environment.apiUrl}/api/examAttempts?size=10000`).pipe(
      map((r: any): ExamAttempt[] => Array.isArray(r) ? r : (r?.content ?? []))
    );
  }

  getById(id: number) {
    return this.http.get<ExamAttempt>(`${environment.apiUrl}/api/examAttempts/${id}`);
  }

  delete(id: number) {
    return this.http.delete<ExamAttempt>(`${environment.apiUrl}/api/examAttempts/${id}`);
  }

  update(id: number, examAttempt: ExamAttempt) {
    return this.http.put<ExamAttempt>(`${environment.apiUrl}/api/examAttempts/${id}`, examAttempt);
  }

  create(payload: { courseId: number; startTime?: Date; endTime?: Date; note?: string }) {
    return this.http.post<ExamAttempt>(`${environment.apiUrl}/api/examAttempts/register`, payload);
  }

  getRegisteredByStudent(id: number) {
    return this.http.get<any[]>(`${environment.apiUrl}/api/examAttempts/registered/${id}`);
  }

  getRegisteredByCourse(id: number) {
    return this.http.get<any[]>(`${environment.apiUrl}/api/examAttempts/registered-by-course/${id}`);
  }
}
