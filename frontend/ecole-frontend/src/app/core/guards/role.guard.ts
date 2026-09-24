import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard = (roles: string[]): CanActivateFn => (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }

  if (!roles || roles.length === 0) {
    return true;
  }

  if (authService.hasAnyRole(roles)) {
    return true;
  }

  router.navigate(['/dashboard']);
  return false;
};
