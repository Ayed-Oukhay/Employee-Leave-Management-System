import { ITypeContrat } from 'app/entities/type-contrat/type-contrat.model';
import { ISalarie } from 'app/entities/salarie/salarie.model';

export interface IContrat {
  id?: number;
  typeContrat?: ITypeContrat | null;
  salarie?: ISalarie | null;
}

export class Contrat implements IContrat {
  constructor(public id?: number, public typeContrat?: ITypeContrat | null, public salarie?: ISalarie | null) {}
}

export function getContratIdentifier(contrat: IContrat): number | undefined {
  return contrat.id;
}
