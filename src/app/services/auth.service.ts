import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  apiURL: string = 'http://localhost:8081/users';
  token!: string;
  private helper = new JwtHelperService();

  public loggedUser!: string;
  public isloggedIn: Boolean = false;
  public roles!: string[];
  public regitredUser: User = new User();

  constructor(private router: Router, private http: HttpClient) {
    this.loadToken();
  }

  setRegistredUser(user: User) {
    this.regitredUser = user;
  }

  getRegistredUser() {
    return this.regitredUser;
  }

  login(user: User) {
    return this.http.post<User>(this.apiURL + '/login', user, { observe: 'response' });
  }

  registerUser(user: User) {
    return this.http.post<User>(this.apiURL + '/register', user, { observe: 'response' });
  }

  validateEmail(code: string) {
    return this.http.get<User>(this.apiURL + '/verifyEmail/' + encodeURIComponent(code));
  }

  saveToken(jwt: string) {
    localStorage.setItem('jwt', jwt);
    this.token = jwt;
    this.isloggedIn = true;
    this.decodeJWT();
  }

  decodeJWT() {
    if (this.token == undefined)
      return;
    const decodedToken = this.helper.decodeToken(this.token);
    if (!decodedToken)
      return;
    this.roles = decodedToken.roles;
    this.loggedUser = decodedToken.sub;
  }

  loadToken() {
    this.token = localStorage.getItem('jwt')!;
    if (this.token == undefined) {
      this.isloggedIn = false;
      return;
    }
    try {
      this.decodeJWT();
      this.isloggedIn = !this.isTokenExpired();
      if (!this.isloggedIn) {
        this.loggedUser = undefined!;
        this.roles = undefined!;
      }
    } catch {
      this.isloggedIn = false;
      this.loggedUser = undefined!;
      this.roles = undefined!;
    }
  }

  getToken(): string {
    return this.token;
  }

  isTokenExpired(): Boolean {
    if (this.token == undefined)
      return true;
    try {
      return this.helper.isTokenExpired(this.token);
    } catch {
      return true;
    }
  }

  isAdmin(): Boolean {
    if (!this.roles)
      return false;
    return this.roles.indexOf('ADMIN') >= 0;
  }

  logout() {
    this.loggedUser = undefined!;
    this.roles = undefined!;
    this.token = undefined!;
    this.isloggedIn = false;
    localStorage.removeItem('jwt');
    this.router.navigate(['/login']);
  }
}
