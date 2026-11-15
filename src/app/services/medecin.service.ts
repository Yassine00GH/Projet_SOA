import { Injectable } from '@angular/core';
import { Medecin } from '../model/medecin.model';
import { Specialite } from '../model/specialite.model';
import { SpecialiteWrapper } from '../model/specialiteWrapped.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class MedecinService {
  apiURL: string = environment.apiURL;
  apiURLSpec: string = environment.apiURLSpec;

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  private getAuthHeaders(): HttpHeaders {
    let jwt = this.authService.getToken();
    jwt = "Bearer " + jwt;
    return new HttpHeaders({ "Authorization": jwt });
  }

  listeMedecins(): Observable<Medecin[]> {
    let httpHeaders = this.getAuthHeaders();
    return this.http.get<Medecin[]>(this.apiURL + "/all", { headers: httpHeaders });
  }

  ajouterMedecin(med: Medecin): Observable<Medecin> {
    let httpHeaders = this.getAuthHeaders();
    return this.http.post<Medecin>(this.apiURL + "/addmed", med, { headers: httpHeaders });
  }

  supprimerMedecin(id: number) {
    const url = `${this.apiURL}/delmed/${id}`;
    let httpHeaders = this.getAuthHeaders();
    return this.http.delete(url, { headers: httpHeaders });
  }

  consulterMedecin(id: number): Observable<Medecin> {
    const url = `${this.apiURL}/getbyid/${id}`;
    let httpHeaders = this.getAuthHeaders();
    return this.http.get<Medecin>(url, { headers: httpHeaders });
  }

  updateMedecin(med: Medecin): Observable<Medecin> {
    let httpHeaders = this.getAuthHeaders();
    return this.http.put<Medecin>(this.apiURL + "/updatemed", med, { headers: httpHeaders });
  }

  listeSpecialites(): Observable<SpecialiteWrapper> {
    let httpHeaders = this.getAuthHeaders();
    return this.http.get<SpecialiteWrapper>(this.apiURLSpec, { headers: httpHeaders });
  }

  ajouterSpecialite(spec: Specialite): Observable<Specialite> {
    const payload: any = { ...spec };
    if (!payload.id || payload.id === 0) {
      delete payload.id;
    }
    let httpHeaders = this.getAuthHeaders();
    return this.http.post<Specialite>(this.apiURLSpec, payload, { headers: httpHeaders });
  }

  updateSpecialite(spec: Specialite): Observable<Specialite> {
    const url = `${this.apiURLSpec}/${spec.id}`;
    let httpHeaders = this.getAuthHeaders();
    return this.http.put<Specialite>(url, spec, { headers: httpHeaders });
  }

  rechercherParSpecialite(idSpec: number): Observable<Medecin[]> {
    const url = `${this.apiURL}/MedecinSpec/${idSpec}`;
    let httpHeaders = this.getAuthHeaders();
    return this.http.get<Medecin[]>(url, { headers: httpHeaders });
  }

  rechercherParNom(nom: string): Observable<Medecin[]> {
    const url = `${this.apiURL}/medsByName/${encodeURIComponent(nom)}`;
    let httpHeaders = this.getAuthHeaders();
    return this.http.get<Medecin[]>(url, { headers: httpHeaders });
  }
}
