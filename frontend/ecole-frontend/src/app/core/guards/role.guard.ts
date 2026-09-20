import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    const requiredRoles = route.data['roles'] as string[];
    
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login'], {
        queryParams: { returnUrl: state.url }
      });
      return false;
    }

    if (!requiredRoles || requiredRoles.length === 0) {
      return true;
    }

    if (this.authService.hasAnyRole(requiredRoles)) {
      return true;
    }

    this.router.navigate(['/dashboard']);
    return false;
  }
}

export const roleGuard = (roles: string[]): any => {
  return (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
    const authService = new AuthService(
      {} as any,
      {} as any,
      {} as any
    );
    const router = {} as any;
    
    if (!authService.isAuthenticated()) {
      return false;
    }

    if (!roles || roles.length === 0) {
      return true;
    }

    return authService.hasAnyRole(roles);
  };
};
