import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GlobalNotification } from '../model/global-notification';

@Injectable({
  providedIn: 'root'
})
export class GlobalAnnouncementsService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<GlobalNotification[]>(`${environment.apiUrl}/api/obavestenja`);
  }

  getById(id: number){
    return this.http.get<GlobalNotification>(`http://localhost:8080/api/obavestenja/${id}`);
  }

  delete(id: number){
    return this.http.delete<GlobalNotification>(`http://localhost:8080/api/obavestenja/${id}`);
  }

  update(id: number, Notification: GlobalNotification){
    return this.http.put<GlobalNotification>(`http://localhost:8080/api/obavestenja/${id}`, Notification);
  }

  create(Notification: GlobalNotification){
    return this.http.post<GlobalNotification>(`${environment.apiUrl}/api/obavestenja`, Notification);
  }
}
