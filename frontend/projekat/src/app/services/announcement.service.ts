import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs';
import { Notification } from '../model/announcement';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any>(`${environment.apiUrl}/api/course-announcements?size=10000`).pipe(
      map((r: any): Notification[] => Array.isArray(r) ? r : (r?.content ?? []))
    );
  }

  getById(id: number) {
    return this.http.get<Notification>(`${environment.apiUrl}/api/course-announcements/${id}`);
  }

  delete(id: number) {
    return this.http.delete<Notification>(`${environment.apiUrl}/api/course-announcements/${id}`);
  }

  update(id: number, notification: Notification) {
    return this.http.put<Notification>(`${environment.apiUrl}/api/course-announcements/${id}`, notification);
  }

  create(notification: Notification) {
    return this.http.post<Notification>(`${environment.apiUrl}/api/course-announcements`, notification);
  }

  getByCourse(id: number) {
    return this.http.get<Notification[]>(`${environment.apiUrl}/api/course-announcements/by-course/${id}`);
  }
}
