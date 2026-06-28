import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalNotification } from '../model/global-announcement';

@Injectable({
  providedIn: 'root'
})
export class GlobalNotificationsService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<GlobalNotification[]>(`${environment.apiUrl}/api/announcements`);
  }

  getById(id: number) {
    return this.http.get<GlobalNotification>(`${environment.apiUrl}/api/announcements/${id}`);
  }

  delete(id: number) {
    return this.http.delete<GlobalNotification>(`${environment.apiUrl}/api/announcements/${id}`);
  }

  update(id: number, notification: GlobalNotification) {
    return this.http.put<GlobalNotification>(`${environment.apiUrl}/api/announcements/${id}`, notification);
  }

  create(notification: GlobalNotification) {
    return this.http.post<GlobalNotification>(`${environment.apiUrl}/api/announcements`, notification);
  }
}
