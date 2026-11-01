import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Specialite } from '../model/specialite.model';

@Component({
  selector: 'app-update-specialite',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './update-specialite.html',
  styles: ``
})
export class UpdateSpecialite implements OnInit {
  @Input()
  specialite!: Specialite;

  @Input()
  ajout!: boolean;

  @Output()
  specialiteUpdated = new EventEmitter<Specialite>();

  ngOnInit(): void {
    console.log("ngOnInit du composant UpdateSpecialite ", this.specialite);
  }

  saveSpecialite() {
    this.specialiteUpdated.emit(this.specialite);
  }
}

