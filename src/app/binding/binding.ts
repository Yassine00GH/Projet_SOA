import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-binding',
  imports: [FormsModule],
  templateUrl: './binding.html',
  styles: ``,
})
export class Binding implements OnInit {
  titre: string = "Demo du data Binding Interpolation";
  status:boolean=false;
  nom:string="Yassine Ghorbel";

  constructor() { }

  ngOnInit(): void {
  }
  changerTitre(){
    this.titre="mon nouveau titre";
  }
}
