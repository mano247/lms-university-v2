import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Notification } from '../model/obavestenje';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<Notification[]>('http://localhost:8080/api/course-announcements');
  }

  getById(id: number) {
    return this.http.get<Notification>(`http://localhost:8080/api/course-announcements/${id}`);
  }

  delete(id: number) {
    return this.http.delete<Notification>(`http://localhost:8080/api/course-announcements/${id}`);
  }

  update(id: number, notification: Notification) {
    return this.http.put<Notification>(`http://localhost:8080/api/course-announcements/${id}`, notification);
  }

  create(notification: Notification) {
    return this.http.post<Notification>('http://localhost:8080/api/course-announcements', notification);
  }

  getByCourse(id: number) {
    return this.http.get<Notification[]>(`http://localhost:8080/api/course-announcements/by-course/${id}`);
  }
}
