import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { User } from '../model/user.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styles: ``
})
export class Login {
  user = new User();
  err: number = 0;
  message: string = "login ou mot de passe erronés..";

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  onLoggedin() {
    this.err = 0;
    this.message = "login ou mot de passe erronés..";
    this.authService.login(this.user).subscribe({
      next: (data) => {
        let jwToken = data.headers.get('Authorization')!;
        this.authService.saveToken(jwToken);
        this.router.navigate(['/']);
      },
      error: (err: any) => {
        this.err = 1;
        if (err.error?.errorCause == 'disabled') {
          this.message = "Utilisateur désactivé, Veuillez contacter votre Administrateur";
        }
        this.cdr.markForCheck();
      }
    });
  }
}
