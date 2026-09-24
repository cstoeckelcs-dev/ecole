import { Component, Inject, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { SchoolResponse, SchoolRequest, SchoolType, SchoolLevel } from '../../core/models/school.model';

@Component({
  selector: 'app-school-dialog',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './school-dialog.component.html',
  styleUrls: ['./school-dialog.component.scss']
})
export class SchoolDialogComponent {
  private fb = inject(FormBuilder);

  schoolForm: FormGroup;
  isEditMode: boolean;
  readonly schoolTypes = Object.values(SchoolType);
  readonly schoolLevels = Object.values(SchoolLevel);

  constructor(
    public dialogRef: MatDialogRef<SchoolDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit'; school?: SchoolResponse }
  ) {
    this.isEditMode = data.mode === 'edit';
    const school = data.school;

    this.schoolForm = this.fb.group({
      name: [school?.name ?? '', [Validators.required, Validators.maxLength(100)]],
      address: [school?.address ?? '', [Validators.required, Validators.maxLength(255)]],
      city: [school?.city ?? '', [Validators.required, Validators.maxLength(100)]],
      postalCode: [school?.postalCode ?? '', [Validators.required, Validators.maxLength(20)]],
      country: [school?.country ?? '', [Validators.required, Validators.maxLength(100)]],
      email: [school?.email ?? '', [Validators.required, Validators.email]],
      phone: [school?.phone ?? '', [Validators.required, Validators.maxLength(20)]],
      website: [school?.website ?? '', Validators.maxLength(255)],
      foundedDate: [school?.foundedDate ? new Date(school.foundedDate) : null, [Validators.required]],
      principalName: [school?.principalName ?? '', [Validators.required, Validators.maxLength(100)]],
      principalEmail: [school?.principalEmail ?? '', [Validators.required, Validators.email]],
      principalPhone: [school?.principalPhone ?? '', [Validators.required, Validators.maxLength(20)]],
      accreditationNumber: [school?.accreditationNumber ?? '', [Validators.required, Validators.maxLength(50)]],
      licenseNumber: [school?.licenseNumber ?? '', [Validators.required, Validators.maxLength(50)]],
      description: [school?.description ?? '', [Validators.required, Validators.maxLength(2000)]],
      type: [school?.type ?? SchoolType.PUBLIC, Validators.required],
      level: [school?.level ?? SchoolLevel.PRIMARY, Validators.required]
    });
  }

  onSave(): void {
    if (this.schoolForm.invalid) {
      this.schoolForm.markAllAsTouched();
      return;
    }
    this.dialogRef.close(this.schoolForm.value as SchoolRequest);
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
