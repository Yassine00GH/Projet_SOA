import { CommonModule, DatePipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Medecin } from '../model/medecin.model';
import { MedecinService } from '../services/medecin.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-medecins',
  standalone: true,
  imports: [CommonModule, DatePipe, RouterLink],
  templateUrl: './medecins.html',
  styleUrl: './medecins.css',
})
export class Medecins implements OnInit {
  medecins: Medecin[] = [];

  constructor(
    private medecinService: MedecinService,
    public authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.chargerMedecins();
  }

  chargerMedecins() {
    this.medecinService.listeMedecins().subscribe({
      next: meds => {
        console.log(meds);
        this.medecins = meds;
        this.cdr.markForCheck();
      },
      error: err => {
        console.error('Erreur chargement médecins', err);
      }
    });
  }

  supprimerMedecin(m: Medecin) {
    let conf = confirm("Etes-vous sûr ?");
    if (conf)
      this.medecinService.supprimerMedecin(m.id!).subscribe(() => {
        console.log("médecin supprimé");
        this.chargerMedecins();
      });
  }
}
