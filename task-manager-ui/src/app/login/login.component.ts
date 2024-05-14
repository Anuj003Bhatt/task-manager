import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  isLogin: Boolean=true;
  @ViewChild('loginForm') form: NgForm;
  @ViewChild('signUpForm') signUpForm: NgForm;
  login_userName:string;
  login_password: string;
  login_error:string;


  signup_name: string;
  signup_email: string;
  signup_phone: string;
  singup_error: string;

  constructor(
    private router: Router
  ){}

  onSubmitLogin() {
    console.log(`Login Submitted`)
    this.router.navigate(['/dashboard']);
  }

  onSubmitSignup() {
    console.log(`Signup Submitted`)
    this.router.navigate(['/dashboard']);
  }

  signUpRequest() {
    this.isLogin = false;
  }

  loginRequest() {
    this.isLogin = true;
  }
}
