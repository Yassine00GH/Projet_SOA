import { CommonModule, DatePipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Medecin } from '../model/medecin.model';
import { Specialite } from '../model/specialite.model';
import { ActivatedRoute, Router } from '@angular/router';
import { MedecinService } from '../services/medecin.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-medecin',
  standalone: true,
  imports: [CommonModule, DatePipe, FormsModule],
  templateUrl: './update-medecin.html',
  styles: ``
})
export class UpdateMedecin implements OnInit {
  currentMedecin = new Medecin();
  specialites: Specialite[] = [];
  updatedSpecId! : number;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private medecinService: MedecinService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.medecinService.listeSpecialites().subscribe(specs => {
      console.log(specs);
      this.specialites = specs._embedded.specialites;
      this.cdr.markForCheck();
    });

    this.medecinService.consulterMedecin(this.activatedRoute.snapshot.params['id']).subscribe({
      next: med => {
        this.currentMedecin = med;
        this.updatedSpecId = this.currentMedecin.specialite?.id!;
        this.cdr.markForCheck();
      },
      error: err => {
        console.error('Erreur chargement médecin - Statut:', err.status, err.message);
      }
    });
  }

  onDateChange(value: string) {
    this.currentMedecin.dateDiplome = value ? new Date(value) : undefined;
  }

  updateMedecin() {
    if (!this.currentMedecin.id) {
      this.router.navigate(['/medecins']);
      return;
    }
    if (this.updatedSpecId) {
      const selectedSpec = this.specialites.find(spec => spec.id == this.updatedSpecId);
      this.currentMedecin.specialite = selectedSpec || ({ id: Number(this.updatedSpecId) } as Specialite);
    }
    this.medecinService.updateMedecin(this.currentMedecin).subscribe({
      next: med => {
        this.router.navigate(['/medecins']);
      },
      error: err => {
        console.error('Erreur modification médecin - Statut HTTP:', err.status, 'Message:', err.error || err.message);
      }
    });
  }
}
