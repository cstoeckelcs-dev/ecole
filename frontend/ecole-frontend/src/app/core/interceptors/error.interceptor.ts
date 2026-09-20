import { Injectable, inject } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  private toastr = inject(ToastrService);
  private router = inject(Router);

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        switch (error.status) {
          case 0:
            this.toastr.error('Network error. Please check your connection.');
            break;
          case 400:
            if (error.error && error.error.message) {
              this.toastr.error(error.error.message);
            } else {
              this.toastr.error('Bad request. Please check your input.');
            }
            break;
          case 401:
            // Handled by AuthInterceptor
            break;
          case 403:
            this.toastr.error('You do not have permission to perform this action.');
            break;
          case 404:
            this.toastr.error('Resource not found.');
            break;
          case 422:
            this.toastr.error('Validation error. Please check your input.');
            break;
          case 500:
            this.toastr.error('An unexpected error occurred. Please try again.');
            break;
          default:
            this.toastr.error('An error occurred. Please try again.');
        }
        
        if (error.status === 401 || error.status === 403) {
          this.router.navigate(['/login']);
        }
        
        return throwError(() => error);
      })
    );
  }
}
