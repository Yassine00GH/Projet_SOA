import { Injectable } from '@angular/core';
import { Medecin } from '../model/medecin.model';
import { Specialite } from '../model/specialite.model';
import { SpecialiteWrapper } from '../model/specialiteWrapped.model';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';

const httpOptions = {
  headers: new HttpHeaders( {'Content-Type': 'application/json'} )
};

@Injectable({
  providedIn: 'root'
})
export class MedecinService {
  apiURL: string = environment.apiURL;
  apiURLSpec: string = environment.apiURLSpec;

  constructor(private http: HttpClient) {
  }

  listeMedecins(): Observable<Medecin[]> {
    return this.http.get<any>(this.apiURL).pipe(
      map(res => {
        const items = res?._embedded?.medecins || res;
        return Array.isArray(items) ? items : [];
      })
    );
  }

  ajouterMedecin(med: Medecin): Observable<Medecin> {
    return this.http.post<Medecin>(this.apiURL, med, httpOptions);
  }

  supprimerMedecin(id: number) {
    const url = `${this.apiURL}/${id}`;
    return this.http.delete(url, httpOptions);
  }

  consulterMedecin(id: number): Observable<Medecin> {
    const url = `${this.apiURL}/${id}`;
    return this.http.get<Medecin>(url);
  }

  updateMedecin(med: Medecin): Observable<Medecin> {
    const url = `${this.apiURL}/${med.id}`;
    return this.http.put<Medecin>(url, med, httpOptions);
  }

  listeSpecialites(): Observable<SpecialiteWrapper> {
    return this.http.get<SpecialiteWrapper>(this.apiURLSpec);
  }

  ajouterSpecialite(spec: Specialite): Observable<Specialite> {
    const payload: any = { ...spec };
    if (!payload.id || payload.id === 0) {
      delete payload.id;
    }
    return this.http.post<Specialite>(this.apiURLSpec, payload, httpOptions);
  }

  updateSpecialite(spec: Specialite): Observable<Specialite> {
    const url = `${this.apiURLSpec}/${spec.id}`;
    return this.http.put<Specialite>(url, spec, httpOptions);
  }

  rechercherParSpecialite(idSpec: number): Observable<Medecin[]> {
    const url = `${this.apiURL}/MedecinSpec/${idSpec}`;
    return this.http.get<Medecin[]>(url);
  }

  rechercherParNom(nom: string): Observable<Medecin[]> {
    const url = `${this.apiURL}/medsByName/${encodeURIComponent(nom)}`;
    return this.http.get<Medecin[]>(url);
  }
}
