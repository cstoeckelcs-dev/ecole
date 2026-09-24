import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SchoolService } from '../../core/services/school.service';
import { AuthService } from '../../core/services/auth.service';
import {
  SchoolResponse,
  SchoolRequest,
  SchoolStats,
  SchoolType,
  SchoolLevel
} from '../../core/models/school.model';
import { SchoolDialogComponent } from './school-dialog.component';

@Component({
  selector: 'app-schools',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    MatTooltipModule,
    MatSnackBarModule
  ],
  templateUrl: './schools.component.html',
  styleUrls: ['./schools.component.scss']
})
export class SchoolsComponent implements OnInit {
  private schoolService = inject(SchoolService);
  private authService = inject(AuthService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['name', 'city', 'country', 'type', 'level', 'status', 'actions'];
  dataSource = new MatTableDataSource<SchoolResponse>([]);
  schools: SchoolResponse[] = [];
  stats: SchoolStats | null = null;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  searchName = '';
  isAdmin = this.authService.hasAnyRole(['SUPER_ADMIN', 'ADMIN']);
  readonly schoolTypes = Object.values(SchoolType);
  readonly schoolLevels = Object.values(SchoolLevel);

  ngOnInit(): void {
    this.loadSchools();
    this.loadStats();
  }

  loadSchools(): void {
    this.schoolService
      .getAll({ name: this.searchName, page: this.pageIndex, size: this.pageSize })
      .subscribe({
        next: (page) => {
          this.schools = page.content;
          this.dataSource.data = page.content;
          this.totalElements = page.totalElements;
        },
        error: () => this.showMessage('Erreur lors du chargement des écoles')
      });
  }

  loadStats(): void {
    this.schoolService.getStats().subscribe({
      next: (stats) => (this.stats = stats),
      error: () => {
        this.stats = null;
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadSchools();
  }

  applyFilter(): void {
    this.pageIndex = 0;
    this.loadSchools();
  }

  clearFilter(): void {
    this.searchName = '';
    this.applyFilter();
  }

  openAddDialog(): void {
    const dialogRef = this.dialog.open(SchoolDialogComponent, {
      width: '700px',
      data: { mode: 'create' }
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.schoolService.create(result as SchoolRequest).subscribe({
          next: () => {
            this.showMessage('École créée avec succès');
            this.loadSchools();
            this.loadStats();
          },
          error: () => this.showMessage('Erreur lors de la création de l\'école')
        });
      }
    });
  }

  openEditDialog(school: SchoolResponse): void {
    const dialogRef = this.dialog.open(SchoolDialogComponent, {
      width: '700px',
      data: { mode: 'edit', school }
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.schoolService.update(school.id, result as SchoolRequest).subscribe({
          next: () => {
            this.showMessage('École mise à jour');
            this.loadSchools();
          },
          error: () => this.showMessage('Erreur lors de la mise à jour')
        });
      }
    });
  }

  deleteSchool(school: SchoolResponse): void {
    if (confirm(`Supprimer l'école "${school.name}" ?`)) {
      this.schoolService.delete(school.id).subscribe({
        next: () => {
          this.showMessage('École supprimée');
          this.loadSchools();
          this.loadStats();
        },
        error: () => this.showMessage('Erreur lors de la suppression')
      });
    }
  }

  approveSchool(school: SchoolResponse): void {
    this.schoolService.approve(school.id).subscribe({
      next: () => {
        this.showMessage('École approuvée');
        this.loadSchools();
        this.loadStats();
      },
      error: () => this.showMessage("Erreur lors de l'approbation")
    });
  }

  rejectSchool(school: SchoolResponse): void {
    const reason = prompt('Motif du rejet :');
    if (reason) {
      this.schoolService.reject(school.id, reason).subscribe({
        next: () => {
          this.showMessage('École rejetée');
          this.loadSchools();
          this.loadStats();
        },
        error: () => this.showMessage('Erreur lors du rejet')
      });
    }
  }

  private showMessage(message: string): void {
    this.snackBar.open(message, 'Fermer', { duration: 4000 });
  }
}
