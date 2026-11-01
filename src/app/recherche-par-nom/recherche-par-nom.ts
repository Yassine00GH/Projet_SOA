import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Medecin } from '../model/medecin.model';
import { MedecinService } from '../services/medecin.service';
import { SearchFilterPipe } from '../search-filter-pipe';

@Component({
  selector: 'app-recherche-par-nom',
  standalone: true,
  imports: [FormsModule, CommonModule, SearchFilterPipe],
  templateUrl: './recherche-par-nom.html',
  styles: ``
})
export class RechercheParNom implements OnInit {
  nomMedecin!: string;
  medecins: Medecin[] = [];
  allMedecins: Medecin[] = [];
  searchTerm!: string;

  constructor(
    private medecinService: MedecinService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.medecinService.listeMedecins().subscribe(meds => {
      console.log(meds);
      this.medecins = meds;
      this.allMedecins = meds;
      this.cdr.markForCheck();
    });
  }

  // 1) Recherche par nom en utilisant l'API (bouton Rechercher)
  rechercherMeds() {
    if (this.nomMedecin)
      this.medecinService.rechercherParNom(this.nomMedecin).subscribe(meds => {
        console.log(meds);
        this.medecins = meds;
        this.cdr.markForCheck();
      });
    else
      this.medecinService.listeMedecins().subscribe(meds => {
        console.log(meds);
        this.medecins = meds;
        this.cdr.markForCheck();
      });
  }

  // 2) Recherche par nom en utilisant l'event keyup
  onKeyUp(filterText: string) {
    this.medecins = this.allMedecins.filter(item =>
      (item.nom ?? '').toLowerCase().includes(filterText.toLowerCase()));
  }
}

