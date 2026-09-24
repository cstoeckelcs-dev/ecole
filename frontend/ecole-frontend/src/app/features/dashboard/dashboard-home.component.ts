import { Component, OnInit, inject } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { SchoolService } from '../../core/services/school.service';
import { AuthService } from '../../core/services/auth.service';
import { SchoolStats } from '../../core/models/school.model';

@Component({
  selector: 'app-dashboard-home',
  standalone: true,
  imports: [MatCardModule, MatIconModule, RouterLink],
  templateUrl: './dashboard-home.component.html',
  styleUrls: ['./dashboard-home.component.scss']
})
export class DashboardHomeComponent implements OnInit {
  private schoolService = inject(SchoolService);
  private authService = inject(AuthService);

  stats: SchoolStats | null = null;
  canManageSchools = this.authService.hasAnyRole(['SUPER_ADMIN', 'ADMIN']);
  loading = true;

  get userFirstName(): string {
    return this.authService.currentUser?.firstName ?? this.authService.currentUser?.email ?? '';
  }

  ngOnInit(): void {
    this.schoolService.getStats().subscribe({
      next: (stats) => {
        this.stats = stats;
        this.loading = false;
      },
      error: () => {
        this.stats = null;
        this.loading = false;
      }
    });
  }
}
