import { Injectable, inject } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, switchMap, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { CookieService } from 'ngx-cookie-service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private authService = inject(AuthService);
  private router = inject(Router);
  private cookieService = inject(CookieService);

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const accessToken = this.cookieService.get('accessToken');
    
    if (accessToken) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${accessToken}`
        }
      });
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          const refreshToken = this.cookieService.get('refreshToken');
          
          if (refreshToken) {
            return this.authService.refreshToken().pipe(
              switchMap(() => {
                const newAccessToken = this.cookieService.get('accessToken');
                const newRequest = request.clone({
                  setHeaders: {
                    Authorization: `Bearer ${newAccessToken}`
                  }
                });
                return next.handle(newRequest);
              }),
              catchError((refreshError) => {
                this.authService.logout();
                return throwError(() => refreshError);
              })
            );
          } else {
            this.authService.logout();
            return throwError(() => error);
          }
        }
        
        if (error.status === 403) {
          this.router.navigate(['/dashboard']);
        }
        
        return throwError(() => error);
      })
    );
  }
}
