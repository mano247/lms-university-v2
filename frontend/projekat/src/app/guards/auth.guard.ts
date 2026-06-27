import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // isSessionActive() checks both user profile AND in-memory token.
  // After a page refresh the token is gone even if profile is in localStorage,
  // so this immediately redirects to /login instead of letting the component
  // mount and fail on the first API call.
  if (authService.isSessionActive()) return true;
  return router.createUrlTree(['/login']);
};
