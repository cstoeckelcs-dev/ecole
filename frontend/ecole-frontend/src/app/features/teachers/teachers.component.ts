import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatMenuModule } from '@angular/material/menu';
import { TeacherService } from '../../core/services/teacher.service';
import { TeacherResponse } from '../../core/models/teacher.model';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-teachers',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatPaginatorModule,
    MatSnackBarModule,
    MatMenuModule
  ],
  templateUrl: './teachers.component.html',
  styleUrls: ['./teachers.component.scss']
})
export class TeachersComponent implements OnInit {
  private teacherService = inject(TeacherService);
  private authService = inject(AuthService);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['firstName', 'lastName', 'email', 'specialization', 'status'];
  dataSource = new MatTableDataSource<TeacherResponse>([]);
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  isAdmin = this.authService.hasAnyRole(['SUPER_ADMIN', 'ADMIN']);

  ngOnInit(): void {
    this.loadTeachers();
  }

  loadTeachers(): void {
    this.teacherService.getAll({ page: this.pageIndex, size: this.pageSize }).subscribe({
      next: (page) => {
        this.dataSource.data = page.content;
        this.totalElements = page.totalElements;
      },
      error: () => this.snackBar.open('Erreur lors du chargement des enseignants', 'Fermer', { duration: 4000 })
    });
  }

  onPageChange(event: any): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadTeachers();
  }
}
