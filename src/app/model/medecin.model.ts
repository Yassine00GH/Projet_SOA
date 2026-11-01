import { Specialite } from './specialite.model';

export class Medecin {
  id?: number;
  nom?: string;
  prenom?: string;
  email?: string;
  telephone?: string;
  dateDiplome?: Date;
  specialite?: Specialite;
}
