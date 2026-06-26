import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalNotification } from '../model/global-notification';
import { Notification } from '../model/notification';

@Injectable({
  providedIn: 'root'
})
export class AnnouncementService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Notification[]>(`${environment.apiUrl}/api/predmetnaObavestenja`);
  }

  getById(id: number){
    return this.http.get<Notification>(`http://localhost:8080/api/predmetnaObavestenja/${id}`);
  }

  delete(id: number){
    return this.http.delete<Notification>(`http://localhost:8080/api/predmetnaObavestenja/${id}`);
  }

  update(id: number, Notification: Notification){
    return this.http.put<Notification>(`http://localhost:8080/api/predmetnaObavestenja/${id}`, Notification);
  }

  create(obavestenje: Notification){
    return this.http.post<Notification>(`${environment.apiUrl}/api/predmetnaObavestenja`, Notification);
  }

  getBycourse(id: number){
    return this.http.get<Notification[]>(`http://localhost:8080/api/predmetnaObavestenja/gbp/${id}`)
  }
}
