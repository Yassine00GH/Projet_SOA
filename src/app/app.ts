import { Component, OnInit, signal } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('MedecinProjFront');

  constructor(
    public authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.authService.loadToken();
    if ((this.authService.getToken() == null ||
        this.authService.isTokenExpired()) &&
        !window.location.pathname.startsWith('/register') &&
        !window.location.pathname.startsWith('/verifEmail'))
      this.router.navigate(['/login']);
  }

  onLogout() {
    this.authService.logout();
  }
}
