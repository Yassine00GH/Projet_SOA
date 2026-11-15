import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './services/auth.service';

// Guard : oblige l'utilisateur à être connecté pour naviguer
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isloggedIn)
    return true;
  else
    return router.createUrlTree(['/login']);
};

// Guard : oblige l'utilisateur à être connecté ET ADMIN
export const medecinGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Pas connecté => page de login
  if (!authService.isloggedIn)
    return router.createUrlTree(['/login']);

  // Connecté mais pas ADMIN => page "non autorisé"
  if (authService.isAdmin())
    return true;
  else
    return router.createUrlTree(['/app-forbidden']);
};
