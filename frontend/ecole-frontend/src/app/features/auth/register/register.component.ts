import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink],
  template: '<p>Register page</p><a routerLink="/login">Back to login</a>'
})
export class RegisterComponent {}
