import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES)
  },
  {
    path: 'dashboard',
    loadChildren: () => import('./features/dashboard/dashboard.routes').then(m => m.DASHBOARD_ROUTES),
    canActivate: [authGuard]
  },
  {
    path: 'schools',
    loadChildren: () => import('./features/schools/schools.routes').then(m => m.SCHOOLS_ROUTES),
    canActivate: [authGuard, roleGuard(['SUPER_ADMIN', 'ADMIN'])]
  },
  {
    path: 'teachers',
    loadChildren: () => import('./features/teachers/teachers.routes').then(m => m.TEACHERS_ROUTES),
    canActivate: [authGuard, roleGuard(['SUPER_ADMIN', 'ADMIN', 'USER'])]
  },
  {
    path: 'profile',
    loadChildren: () => import('./features/profile/profile.routes').then(m => m.PROFILE_ROUTES),
    canActivate: [authGuard]
  },
  {
    path: 'settings',
    loadChildren: () => import('./features/settings/settings.routes').then(m => m.SETTINGS_ROUTES),
    canActivate: [authGuard]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
