import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const allowed = route.data['allowedPermissions'] as string[] | undefined;

  if (!allowed || allowed.length === 0) return true;

  if (authService.hasAnyPermission(allowed)) return true;

  return router.createUrlTree(['/unauthorized']);
};
