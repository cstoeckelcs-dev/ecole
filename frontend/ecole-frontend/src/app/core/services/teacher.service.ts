import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Teacher, TeacherRequest, TeacherResponse, TeacherStats, TeacherSearchParams } from '../models/teacher.model';
import { Page } from '../models/page.model';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  private readonly API_URL = 'http://localhost:8080/api/teachers';

  constructor(private http: HttpClient) { }

  getAll(params?: TeacherSearchParams): Observable<Page<TeacherResponse>> {
    let httpParams = new HttpParams();
    
    if (params) {
      if (params.schoolId !== undefined) httpParams = httpParams.append('schoolId', params.schoolId);
      if (params.specialization) httpParams = httpParams.append('specialization', params.specialization);
      if (params.isApproved !== undefined) httpParams = httpParams.append('isApproved', params.isApproved);
      if (params.isActive !== undefined) httpParams = httpParams.append('isActive', params.isActive);
      if (params.page !== undefined) httpParams = httpParams.append('page', params.page);
      if (params.size !== undefined) httpParams = httpParams.append('size', params.size);
    }

    return this.http.get<Page<TeacherResponse>>(this.API_URL, { params: httpParams });
  }

  getById(id: number): Observable<TeacherResponse> {
    return this.http.get<TeacherResponse>(`${this.API_URL}/${id}`);
  }

  create(teacher: TeacherRequest): Observable<TeacherResponse> {
    return this.http.post<TeacherResponse>(this.API_URL, teacher);
  }

  update(id: number, teacher: TeacherRequest): Observable<TeacherResponse> {
    return this.http.put<TeacherResponse>(`${this.API_URL}/${id}`, teacher);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  approve(id: number): Observable<TeacherResponse> {
    return this.http.post<TeacherResponse>(`${this.API_URL}/${id}/approve`, {});
  }

  reject(id: number, reason: string): Observable<TeacherResponse> {
    return this.http.post<TeacherResponse>(`${this.API_URL}/${id}/reject`, {}, {
      params: { reason }
    });
  }

  uploadProfilePicture(id: number, file: File): Observable<TeacherResponse> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<TeacherResponse>(`${this.API_URL}/${id}/profile-picture`, formData);
  }

  getStats(): Observable<TeacherStats> {
    return this.http.get<TeacherStats>(`${this.API_URL}/stats`);
  }

  getQualifications(id: number): Observable<any> {
    return this.http.get(`${this.API_URL}/${id}/qualifications`);
  }

  getExperiences(id: number): Observable<any> {
    return this.http.get(`${this.API_URL}/${id}/experiences`);
  }

  getDocuments(id: number): Observable<any> {
    return this.http.get(`${this.API_URL}/${id}/documents`);
  }
}
