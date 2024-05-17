import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  @ViewChild('loginForm') form: NgForm;
  login_userName: string;
  login_password: string;
  login_error: string;

  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  onSubmitLogin() {
    this.authService.login(this.login_userName, this.login_password).subscribe({
      next: (response) => {
        this.authService.setUser(response['user']);
        this.authService.setToken(response['authToken'])
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.login_error = error.error.error;
      }
    })

  }

  signUpRequest() {
    this.router.navigate(['/signup']);
  }

  renderReset() {
    this.router.navigate(['/reset']);
  }
}
