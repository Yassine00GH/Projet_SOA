import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  users: User[] = [
    { "username": "admin", "password": "123", "roles": ['ADMIN'] },
    { "username": "yassine", "password": "123", "roles": ['USER'] }
  ];

  public loggedUser!: string;
  public isloggedIn: Boolean = false;
  public roles!: string[];

  constructor(private router: Router) {
    // Restaurer l'utilisateur connecté dès la création du service
    // (utile quand on colle un lien ou qu'on rafraîchit la page)
    const isloggedin = localStorage.getItem('isloggedIn');
    const loggedUser = localStorage.getItem('loggedUser');
    if (isloggedin == "true" && loggedUser)
      this.setLoggedUserFromLocalStorage(loggedUser);
  }

  logout() {
    this.isloggedIn = false;
    this.loggedUser = undefined!;
    this.roles = undefined!;
    localStorage.removeItem('loggedUser');
    localStorage.setItem('isloggedIn', String(this.isloggedIn));
    this.router.navigate(['/login']);
  }

  SignIn(user: User): Boolean {
    let validUser: Boolean = false;
    this.users.forEach((curUser) => {
      if (user.username == curUser.username && user.password == curUser.password) {
        validUser = true;
        this.loggedUser = curUser.username;
        this.isloggedIn = true;
        this.roles = curUser.roles;
        localStorage.setItem('loggedUser', this.loggedUser);
        localStorage.setItem('isloggedIn', String(this.isloggedIn));
      }
    });
    return validUser;
  }

  isAdmin(): Boolean {
    if (!this.roles)
      return false;
    return (this.roles.indexOf('ADMIN') > -1);
  }

  setLoggedUserFromLocalStorage(login: string) {
    this.loggedUser = login;
    this.isloggedIn = true;
    this.getUserRoles(login);
  }

  getUserRoles(username: string) {
    this.users.forEach((curUser) => {
      if (curUser.username == username) {
        this.roles = curUser.roles;
      }
    });
  }
}

