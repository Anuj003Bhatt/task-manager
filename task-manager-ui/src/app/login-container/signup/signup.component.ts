import { Component, ViewChild } from "@angular/core";
import { NgForm } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../service/auth.service";

@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html',
    styleUrls: [
        './signup.component.css'
    ]
})
export class SignupComponent {
    @ViewChild('signUpForm') signUpForm: NgForm;
    
    signup_name: string;
    signup_password: string;
    signup_email: string;
    signup_phone: string;
    singup_error: string;
  
    constructor(
      private router: Router,
      private authService: AuthService
    ){}
  
    onSubmitSignup() {
      this.authService.signUp(this.signup_name, this.signup_email, this.signup_phone, this.signup_password)
      .subscribe({
        next: (response) => {
          this.authService.setUser(response['user']);
          this.authService.setToken(response['authToken'])
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.singup_error = error.error.error;
        }
    }) 
    }

    goToLogin() {
        this.router.navigate(['/login']);
    }
}