import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Specialite } from '../model/specialite.model';
import { MedecinService } from '../services/medecin.service';
import { UpdateSpecialite } from '../update-specialite/update-specialite';

@Component({
  selector: 'app-liste-specialites',
  standalone: true,
  imports: [CommonModule, UpdateSpecialite],
  templateUrl: './liste-specialites.html',
  styles: ``
})
export class ListeSpecialites implements OnInit {
  specialites: Specialite[] = [];
  updatedSpec: Specialite = { id: 0, nom: '', description: '' };
  ajout: boolean = true;

  constructor(
    private medecinService: MedecinService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.chargerSpecialites();
  }

  chargerSpecialites() {
    this.medecinService.listeSpecialites().subscribe(specs => {
      this.specialites = specs._embedded.specialites;
      console.log(specs);
      this.cdr.markForCheck();
    });
  }

  specialiteUpdated(spec: Specialite) {
    console.log("Spec updated event", spec);
    const obs = this.ajout
      ? this.medecinService.ajouterSpecialite(spec)
      : this.medecinService.updateSpecialite(spec);

    obs.subscribe(() => {
      this.chargerSpecialites();
      this.updatedSpec = { id: 0, nom: '', description: '' };
      this.ajout = true;
      this.cdr.markForCheck();
    });
  }

  updateSpec(spec: Specialite) {
    this.updatedSpec = { ...spec };
    this.ajout = false;
  }
}

