import { Routes } from '@angular/router';
import { Medecins } from './medecins/medecins';
import { AddMedecin } from './add-medecin/add-medecin';
import { UpdateMedecin } from './update-medecin/update-medecin';
import { RechercheParSpecialite } from './recherche-par-specialite/recherche-par-specialite';
import { RechercheParNom } from './recherche-par-nom/recherche-par-nom';
import { ListeSpecialites } from './liste-specialites/liste-specialites';
import { Login } from './login/login';
import { Forbidden } from './forbidden/forbidden';
import { medecinGuard } from './medecin.guard';

export const routes: Routes = [
    { path: 'medecins', component: Medecins },
    { path: 'add-medecin', component: AddMedecin, canActivate: [medecinGuard] },
    { path: 'updateMedecin/:id', component: UpdateMedecin, canActivate: [medecinGuard] },
    { path: 'rechercheParSpecialite', component: RechercheParSpecialite },
    { path: 'rechercheParNom', component: RechercheParNom },
    { path: 'listeSpecialites', component: ListeSpecialites },
    { path: 'login', component: Login },
    { path: 'app-forbidden', component: Forbidden },
    { path: '', redirectTo: '/medecins', pathMatch: 'full' }
];
