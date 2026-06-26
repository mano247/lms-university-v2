import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

/**
 * Thin wrapper around AuthService kept for backward compatibility.
 * New components should inject AuthService directly.
 */
@Injectable({ providedIn: 'root' })
export class LoginService {
  constructor(private authService: AuthService) {}

  loginUser(data: { email: string; password?: string }): Observable<any> {
    const password = data.password ?? '';
    return this.authService.login(data.email, password);
  }

  registerUser(data: any): Observable<any> {
    return this.authService.register(data);
  }

  validateRoles(roles: string[]): boolean {
    return this.authService.hasAnyPermission(roles);
  }

  loggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  logout(): void {
    this.authService.logout();
  }

  getUserRole(): string[] {
    return this.authService.getCurrentUser()?.permissions ?? [];
  }

  getToken(): string | null {
    return this.authService.getToken();
  }
}
