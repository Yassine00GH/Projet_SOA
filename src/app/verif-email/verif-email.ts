import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../model/user.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-verif-email',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './verif-email.html',
  styleUrl: './verif-email.css'
})
export class VerifEmail implements OnInit {
  code: string = '';
  user: User = new User();
  err = '';

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.user = this.authService.regitredUser;
  }

  onValidateEmail() {
    this.err = '';
    this.authService.validateEmail(this.code).subscribe({
      next: (res) => {
        alert('Login successful');
        if (this.user?.username && this.user?.password) {
          this.authService.login(this.user).subscribe({
            next: (data) => {
              let jwToken = data.headers.get('Authorization')!;
              this.authService.saveToken(jwToken);
              this.router.navigate(['/']);
            },
            error: (err: any) => {
              console.log(err);
            }
          });
        } else {
          this.router.navigate(['/login']);
        }
      },
      error: (err: any) => {
        if (err.error?.errorCode == 'INVALID_TOKEN') {
          this.err = 'Code invalide!';
        } else if (err.error?.errorCode == 'EXPIRED_TOKEN') {
          this.err = 'Code a expiré!';
        } else if (err.status === 400 || err.status === 404) {
          this.err = err.error?.message;
        }
        console.log(err.error?.errorCode || err);
        this.cdr.markForCheck();
      }
    });
  }
}

