import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface User {
  id: number;
  email: string;
  username: string;
  permissions: string[];
}

export type Permission =
  | 'STUDENT_PERMISSION'
  | 'TEACHER_PERMISSION'
  | 'STUDENT_AFFAIRS_PERMISSION'
  | 'ADMINISTRATOR_PERMISSION';

interface LoginResponse {
  id: number;
  email: string;
  username: string;
  accessToken: string;
  permissions: string[];
  [key: string]: any;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private _token: string | null = null;
  private currentUserSubject = new BehaviorSubject<User | null>(null);

  readonly currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    const stored = localStorage.getItem('currentUser');
    if (stored) {
      try {
        this.currentUserSubject.next(JSON.parse(stored));
        // _token is null after a page refresh — API calls will get 401
        // and the interceptor will redirect to /login
      } catch {
        localStorage.removeItem('currentUser');
      }
    }
  }

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${environment.apiUrl}/api/auth/signin`, { email, password })
      .pipe(
        tap(response => {
          this._token = response.accessToken;
          const user: User = {
            id: response.id,
            email: response.email,
            username: response.username,
            permissions: response.permissions ?? [],
          };
          this.currentUserSubject.next(user);
          localStorage.setItem('currentUser', JSON.stringify(user));
        }),
      );
  }

  register(data: {
    firstName: string;
    lastName: string;
    username: string;
    email: string;
    password: string;
  }): Observable<any> {
    return this.http.post(`${environment.apiUrl}/api/auth/signup`, data);
  }

  logout(): void {
    this._token = null;
    this.currentUserSubject.next(null);
    localStorage.removeItem('currentUser');
    localStorage.removeItem('selectedTabIndex');
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this._token;
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    return this.currentUserSubject.value !== null;
  }

  hasPermission(permission: string): boolean {
    return this.currentUserSubject.value?.permissions.includes(permission) ?? false;
  }

  hasAnyPermission(permissions: string[]): boolean {
    return permissions.some(p => this.hasPermission(p));
  }
}
