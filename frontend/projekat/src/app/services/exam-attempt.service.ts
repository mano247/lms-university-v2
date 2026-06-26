import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ExamAttempt } from '../model/exam-attempt';

@Injectable({
  providedIn: 'root'
})
export class ExamAttemptService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<ExamAttempt[]>(`${environment.apiUrl}/api/polaganja`);
  }

  getById(id: number){
    return this.http.get<ExamAttempt>(`${environment.apiUrl}/api/polaganja/${id}`);
  }

  delete(id: number){
    return this.http.delete<ExamAttempt>(`${environment.apiUrl}/api/polaganja/${id}`);
  }

  update(id: number, examAttempt: ExamAttempt){
    return this.http.put<ExamAttempt>(`${environment.apiUrl}/api/polaganja/${id}`, examAttempt);
  }

  create(examAttempt: ExamAttempt){
    return this.http.post<ExamAttempt>(`${environment.apiUrl}/api/polaganja/c`, examAttempt);
  }

  getPrijavljeni(id: number){
    return this.http.get<any[]>(`${environment.apiUrl}/api/polaganja/prijavljeni/${id}`);
  }

  getPrijavljeniPocourseu(id: number){
    return this.http.get<any[]>(`${environment.apiUrl}/api/polaganja/prijavljeniPocourseu/${id}`);
  }
}
