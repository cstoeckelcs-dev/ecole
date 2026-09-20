import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { School, SchoolRequest, SchoolResponse, SchoolStats, SchoolSearchParams } from '../models/school.model';
import { Page } from '../models/page.model';

@Injectable({
  providedIn: 'root'
})
export class SchoolService {
  private readonly API_URL = 'http://localhost:8080/api/schools';

  constructor(private http: HttpClient) { }

  getAll(params?: SchoolSearchParams): Observable<Page<SchoolResponse>> {
    let httpParams = new HttpParams();
    
    if (params) {
      if (params.name) httpParams = httpParams.append('name', params.name);
      if (params.city) httpParams = httpParams.append('city', params.city);
      if (params.country) httpParams = httpParams.append('country', params.country);
      if (params.type) httpParams = httpParams.append('type', params.type);
      if (params.level) httpParams = httpParams.append('level', params.level);
      if (params.isApproved !== undefined) httpParams = httpParams.append('isApproved', params.isApproved);
      if (params.isActive !== undefined) httpParams = httpParams.append('isActive', params.isActive);
      if (params.page !== undefined) httpParams = httpParams.append('page', params.page);
      if (params.size !== undefined) httpParams = httpParams.append('size', params.size);
    }

    return this.http.get<Page<SchoolResponse>>(this.API_URL, { params: httpParams });
  }

  getById(id: number): Observable<SchoolResponse> {
    return this.http.get<SchoolResponse>(`${this.API_URL}/${id}`);
  }

  create(school: SchoolRequest): Observable<SchoolResponse> {
    return this.http.post<SchoolResponse>(this.API_URL, school);
  }

  update(id: number, school: SchoolRequest): Observable<SchoolResponse> {
    return this.http.put<SchoolResponse>(`${this.API_URL}/${id}`, school);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  approve(id: number): Observable<SchoolResponse> {
    return this.http.post<SchoolResponse>(`${this.API_URL}/${id}/approve`, {});
  }

  reject(id: number, reason: string): Observable<SchoolResponse> {
    return this.http.post<SchoolResponse>(`${this.API_URL}/${id}/reject`, {}, {
      params: { reason }
    });
  }

  uploadLogo(id: number, file: File): Observable<SchoolResponse> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<SchoolResponse>(`${this.API_URL}/${id}/logo`, formData);
  }

  getStats(): Observable<SchoolStats> {
    return this.http.get<SchoolStats>(`${this.API_URL}/stats`);
  }

  getTeachers(id: number, page?: number, size?: number): Observable<Page<any>> {
    let params = new HttpParams();
    if (page !== undefined) params = params.append('page', page);
    if (size !== undefined) params = params.append('size', size);
    return this.http.get<Page<any>>(`${this.API_URL}/${id}/teachers`, { params });
  }
}
