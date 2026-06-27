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
  private _logoutTimer: ReturnType<typeof setTimeout> | null = null;
  private currentUserSubject = new BehaviorSubject<User | null>(null);

  readonly currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    // Restore user profile for UI continuity, but _token stays null.
    // The first API call after a page refresh will return 401 and the
    // interceptor will call logout() → redirect to /login.
    const stored = localStorage.getItem('currentUser');
    if (stored) {
      try {
        this.currentUserSubject.next(JSON.parse(stored));
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
          this.scheduleTokenExpiry(response.accessToken);
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

  logout(reason?: 'session-expired'): void {
    if (this._logoutTimer !== null) {
      clearTimeout(this._logoutTimer);
      this._logoutTimer = null;
    }
    this._token = null;
    this.currentUserSubject.next(null);
    localStorage.removeItem('currentUser');
    localStorage.removeItem('selectedTabIndex');
    if (reason === 'session-expired') {
      sessionStorage.setItem('logoutReason', 'session-expired');
    }
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this._token;
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  /** True only when BOTH user profile and in-memory token are present. */
  isSessionActive(): boolean {
    return this.currentUserSubject.value !== null && this._token !== null;
  }

  /** Legacy alias — kept for components that only need to check profile presence. */
  isLoggedIn(): boolean {
    return this.currentUserSubject.value !== null;
  }

  hasPermission(permission: string): boolean {
    return this.currentUserSubject.value?.permissions.includes(permission) ?? false;
  }

  hasAnyPermission(permissions: string[]): boolean {
    return permissions.some(p => this.hasPermission(p));
  }

  // ─── JWT helpers ────────────────────────────────────────────────────────────

  private parseJwtPayload(token: string): { exp?: number } | null {
    try {
      const payloadB64 = token.split('.')[1];
      // base64url → standard base64 → JSON
      const decoded = atob(payloadB64.replace(/-/g, '+').replace(/_/g, '/'));
      return JSON.parse(decoded) as { exp?: number };
    } catch {
      return null;
    }
  }

  private scheduleTokenExpiry(token: string): void {
    const payload = this.parseJwtPayload(token);
    if (!payload?.exp) return;

    const msUntilExpiry = payload.exp * 1000 - Date.now();

    if (msUntilExpiry <= 0) {
      // Token already expired on arrival — log out immediately.
      this.logout('session-expired');
      return;
    }

    this._logoutTimer = setTimeout(() => {
      this.logout('session-expired');
    }, msUntilExpiry);
  }
}
