import { Routes } from '@angular/router';
import { Medecins } from './medecins/medecins';
import { AddMedecin } from './add-medecin/add-medecin';
import { UpdateMedecin } from './update-medecin/update-medecin';
import { RechercheParSpecialite } from './recherche-par-specialite/recherche-par-specialite';
import { RechercheParNom } from './recherche-par-nom/recherche-par-nom';
import { ListeSpecialites } from './liste-specialites/liste-specialites';
import { Login } from './login/login';
import { Forbidden } from './forbidden/forbidden';
import { Register } from './register/register';
import { VerifEmail } from './verif-email/verif-email';
import { authGuard, medecinGuard } from './medecin.guard';

export const routes: Routes = [
    { path: 'medecins', component: Medecins, canActivate: [authGuard] },
    { path: 'add-medecin', component: AddMedecin, canActivate: [medecinGuard] },
    { path: 'updateMedecin/:id', component: UpdateMedecin, canActivate: [medecinGuard] },
    { path: 'rechercheParSpecialite', component: RechercheParSpecialite, canActivate: [authGuard] },
    { path: 'rechercheParNom', component: RechercheParNom, canActivate: [authGuard] },
    { path: 'listeSpecialites', component: ListeSpecialites, canActivate: [authGuard] },
    { path: 'login', component: Login },
    { path: 'register', component: Register },
    { path: 'verifEmail', component: VerifEmail },
    { path: 'app-forbidden', component: Forbidden, canActivate: [authGuard] },
    { path: '', redirectTo: '/medecins', pathMatch: 'full' }
];
