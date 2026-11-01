import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Medecin } from '../model/medecin.model';
import { Specialite } from '../model/specialite.model';
import { MedecinService } from '../services/medecin.service';

@Component({
  selector: 'app-recherche-par-specialite',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './recherche-par-specialite.html',
  styles: ``
})
export class RechercheParSpecialite implements OnInit {
  medecins: Medecin[] = [];
  IdSpecialite!: number;
  specialites: Specialite[] = [];

  constructor(
    private medecinService: MedecinService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.medecinService.listeSpecialites().subscribe(specs => {
      console.log(specs);
      this.specialites = specs._embedded.specialites;
      this.cdr.markForCheck();
    });
  }

  onChange() {
    this.medecinService.rechercherParSpecialite(this.IdSpecialite).subscribe(meds => {
      console.log(meds);
      this.medecins = meds;
      this.cdr.markForCheck();
    });
  }
}

