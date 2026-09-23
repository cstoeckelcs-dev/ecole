import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [RouterLink],
  template: '<p>Forgot password page</p><a routerLink="/login">Back to login</a>'
})
export class ForgotPasswordComponent {}
