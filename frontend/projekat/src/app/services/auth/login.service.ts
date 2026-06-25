import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService{
  private user: any;
  private baseUrl = `${environment.apiUrl}`;
  
  constructor(private http: HttpClient, private router: Router) {
    const userString = localStorage.getItem('user');
    this.user = userString ? JSON.parse(userString) : null;
   }
  
  loginUser(data: any){
    return this.http.post(`${this.baseUrl}/api/auth/signin`, data).pipe(
      tap((response) => {
          localStorage.setItem('user', JSON.stringify(response));
          this.user = response;
      })
    );
  }

  registerUser(data: any){
    return this.http.post(`${this.baseUrl}/api/auth/signup`, data);
  }

  validateRoles(roles: string[]): boolean {
    if (this.user) {
      const userRoles = new Set(this.user.permissions);
      for (const r of roles) {
        if (userRoles.has(r)) {
          return true;
        }
      }
    }
    return false;
  }

  loggedIn(): boolean{
    return !!this.user;
  }

  logout(): void{
    localStorage.removeItem('user');
    localStorage.removeItem('selectedTabIndex');
    this.user = null;
    this.router.navigate(['/']);
  }

  getUserRole(): string[] {
    return this.user ? this.user.permissions : [];
  }

  getToken(): string | undefined {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      return parsedUser.accessToken; 
    }
    return undefined;
  }

}
