import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent {
  email: string;
  otp:string;
  message: string;
  newPassword: string;
  stage:number=0;
  error: string;
  verificationId: string;
  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  onReset() {
    this.authService.reset(this.email).subscribe({
      next: (response) => {
        this.error = undefined
        this.message = "OTP has been sent to this email ID."
        this.stage = 1;
      },
      error: (error) => {
        this.message = undefined
        this.error = JSON.stringify(error.error.error);
      }
    })
  }

  onVerify () {
    this.authService.verify(this.email, this.otp).subscribe({
      next: (response) => {
        this.error = undefined
        this.message = "OTP verified successfully"
        this.verificationId = response["id"];
        this.stage = 2;
      },
      error: (error) => {
        this.message = undefined
        this.error = JSON.stringify(error.error.error);
      }
    })  
  }

  onChangePassword () {
    this.authService.changePassword(this.email, this.verificationId, this.newPassword).subscribe({
      next: (response) => {
        this.message = undefined
        this.router.navigate(['/login'])
      },
      error: (error) => {
        this.message = undefined
        this.error = JSON.stringify(error.error);
      }
    })  
  }

  goToLogin() {
    this.router.navigate(['/login'])
  }
}
