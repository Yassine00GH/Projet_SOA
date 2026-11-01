import { CommonModule, DatePipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Medecin } from '../model/medecin.model';
import { Specialite } from '../model/specialite.model';
import { MedecinService } from '../services/medecin.service';

@Component({
  selector: 'app-add-medecin',
  standalone: true,
  imports: [CommonModule, DatePipe, FormsModule],
  templateUrl: './add-medecin.html',
  styleUrl: './add-medecin.css',
})
export class AddMedecin implements OnInit {
  newMedecin = new Medecin();
  specialites: Specialite[] = [];
  newIdSpec! : number;
  newSpecialite! : Specialite;
  message : string = "";

  constructor(
    private medecinService: MedecinService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.medecinService.listeSpecialites().subscribe(specs => {
      console.log(specs);
      this.specialites = specs._embedded.specialites;
      this.cdr.markForCheck();
    });
  }

  onDateChange(value: string) {
    this.newMedecin.dateDiplome = value ? new Date(value) : undefined;
  }

  addMedecin() {
    if (!this.specialites || this.specialites.length === 0) {
      this.message = "Erreur : aucune spécialité chargée. Vérifiez le backend.";
      return;
    }
    if (!this.newIdSpec) {
      this.message = "Erreur : veuillez sélectionner une spécialité.";
      return;
    }
    this.newSpecialite = this.specialites.find(spec => spec.id == this.newIdSpec)!;
    this.newMedecin.specialite = this.newSpecialite;
    if (!this.newMedecin.id) {
      delete this.newMedecin.id;
    }
    this.medecinService.ajouterMedecin(this.newMedecin).subscribe({
      next: med => {
        console.log(med);
        this.message = "Médecin ajouté avec succès !";
        this.router.navigate(['/medecins']);
      },
      error: err => {
        console.error('Erreur ajout médecin', err);
        this.message = "Erreur lors de l'ajout : " + (err.status === 0 ? "CORS / backend indisponible" : err.message);
      }
    });
  }
}
