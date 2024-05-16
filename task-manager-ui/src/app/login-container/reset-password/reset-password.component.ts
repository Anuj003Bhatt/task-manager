import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent {
  email:string;
  reset_error: string;

  constructor(
    private router: Router
  ){}
  onReset() {
    console.log(`Reset Called`)
  }

  goToLogin() {
    this.router.navigate(['/login'])
  }
}
