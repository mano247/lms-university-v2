import { Injectable } from '@angular/core';
import { 
  ActivatedRouteSnapshot, 
  CanActivate, 
  Router, 
  RouterStateSnapshot, 
  UrlTree 
} from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from '../services/auth/login.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private loginService: LoginService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | UrlTree {
    const allowedPermissions = route.data['allowedPermissions'] as string[];
    
    if (this.loginService.validateRoles(allowedPermissions)) {
      return true;
    } else {
      return this.router.createUrlTree(['/']);
    }
  }
}
