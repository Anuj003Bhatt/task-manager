import { HttpClient, HttpHeaders } from "@angular/common/http";
import { EventEmitter, Injectable } from "@angular/core";
import { environment } from "../environment";
import { User } from "../model/user.model";

@Injectable({ providedIn: 'root' })
export class AuthService {
    private loggedInUser: User;
    private authToken: string;
    public logoutEvent = new EventEmitter<void>();

    private headers = {
        'content-type': 'application/json',
        'Access-Control-Allow-Origin': 'http://localhost:4200'
    };

    constructor(
        private httpClient: HttpClient
    ) { }

    login(email: string, password: string) {
        return this.httpClient.post<User>(
            environment.server_url + '/users/login',
            {
                'email': email,
                'password': password
            },
            {
                'headers': this.headers
            }
        );
    }

    signUp(name: string, email: string, phone: string, password: string) {
        return this.httpClient.post<User>(
            environment.server_url + '/users/add',
            {
                'name': name,
                'email': email,
                'phone': phone,
                'password': password
            },
            {
                'headers': this.headers
            }
        );
    }

    reset(email: string) {
        return this.httpClient.post(
            environment.server_url + '/users/reset_password',
            {
                'email': email
            },
            {
                'headers': this.headers
            }
        );
    }

    verify(email: string, otp:string) {
        return this.httpClient.post(
            environment.server_url + '/users/otp_verify',
            {
                'email': email,
                'otp': otp
            },
            {
                'headers': this.headers
            }
        );
    }

    changePassword(email: string, verificationId: string, newPassword: string) {
        return this.httpClient.post<User>(
            environment.server_url + '/users/change_password',
            {
                'email': email,
                'verificationId': verificationId,
                'newPassword': newPassword
            },
            {
                'headers': this.headers
            }
        );
    }

    setToken(token: string) {
        this.authToken = token;
    }

    getToken() {
        return this.authToken;
    }

    setUser(user: User) {
        this.loggedInUser = user;
    }

    getUser() {
        return this.loggedInUser;
    }

    isUserPresent(): boolean {
        return this.loggedInUser != null;
    }

    logout() {
        this.loggedInUser = null;
        this.logoutEvent.emit();
    }
}