import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User, LoginRequest, RegisterRequest, TokenResponse, UserResponse } from '../models/user.model';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router,
    private cookieService: CookieService
  ) {
    this.initialize();
  }

  private initialize(): void {
    const user = this.getUserFromStorage();
    if (user) {
      this.currentUserSubject.next(user);
    }
  }

  login(request: LoginRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${this.API_URL}/login`, request).pipe(
      tap((response) => {
        this.setTokens(response);
        this.getCurrentUser().subscribe();
      })
    );
  }

  register(request: RegisterRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${this.API_URL}/register`, request);
  }

  googleLogin(code: string): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.API_URL}/google/callback?code=${code}`).pipe(
      tap((user) => {
        this.currentUserSubject.next(user as User);
        this.setUserToStorage(user as User);
      })
    );
  }

  getGoogleAuthUrl(): Observable<string> {
    return this.http.get<string>(`${this.API_URL}/google`);
  }

  refreshToken(): Observable<TokenResponse> {
    const refreshToken = this.cookieService.get('refreshToken');
    return this.http.post<TokenResponse>(`${this.API_URL}/refresh`, { refreshToken }).pipe(
      tap((response) => {
        this.setTokens(response);
      })
    );
  }

  logout(): void {
    const refreshToken = this.cookieService.get('refreshToken');
    this.http.post(`${this.API_URL}/logout`, { refreshToken }).subscribe({
      next: () => {
        this.clearAuth();
        this.router.navigate(['/login']);
      },
      error: () => {
        this.clearAuth();
        this.router.navigate(['/login']);
      }
    });
  }

  getCurrentUser(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.API_URL}/me`).pipe(
      tap((user) => {
        this.currentUserSubject.next(user as User);
        this.setUserToStorage(user as User);
      })
    );
  }

  isAuthenticated(): boolean {
    return !!this.cookieService.get('accessToken');
  }

  hasRole(role: string): boolean {
    const user = this.currentUserSubject.value;
    return user?.role === role || user?.authorities?.includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    const user = this.currentUserSubject.value;
    return roles.includes(user?.role || '') || 
           user?.authorities?.some(auth => roles.includes(auth));
  }

  getAccessToken(): string | null {
    return this.cookieService.get('accessToken');
  }

  private setTokens(response: TokenResponse): void {
    this.cookieService.set('accessToken', response.accessToken, {
      expires: new Date(Date.now() + response.expiresIn),
      secure: true,
      sameSite: 'Strict'
    });
    this.cookieService.set('refreshToken', response.refreshToken, {
      expires: 7,
      secure: true,
      sameSite: 'Strict'
    });
  }

  private setUserToStorage(user: User): void {
    localStorage.setItem('currentUser', JSON.stringify(user));
  }

  private getUserFromStorage(): User | null {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user) : null;
  }

  private clearAuth(): void {
    this.cookieService.delete('accessToken');
    this.cookieService.delete('refreshToken');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}
