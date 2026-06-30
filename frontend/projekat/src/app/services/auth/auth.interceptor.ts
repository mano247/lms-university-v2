import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const token = authService.getToken();
  const wasSessionActive = authService.isSessionActive();

  const authReq = token && req.url.includes('/api/')
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && wasSessionActive) {
        authService.logout('session-expired');
      } else if (error.status === 403 && wasSessionActive) {
        ['studentDashboardTab', 'teacherDashboardTab', 'saDashboardTab', 'adminDashboardTab']
          .forEach(k => localStorage.removeItem(k));
        router.navigate(['/unauthorized']);
      }
      return throwError(() => error);
    }),
  );
};
