import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [RouterLink],
  template: '<p>Reset password page</p><a routerLink="/login">Back to login</a>'
})
export class ResetPasswordComponent {}
