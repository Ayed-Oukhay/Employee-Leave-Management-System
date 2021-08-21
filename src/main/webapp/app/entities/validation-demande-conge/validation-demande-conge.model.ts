import * as dayjs from 'dayjs';
import { ITypeConge } from 'app/entities/type-conge/type-conge.model';
import { ISalarie } from 'app/entities/salarie/salarie.model';
import { Etat } from 'app/entities/enumerations/etat.model';

export interface IDemandeConge {
  id?: number;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  duree?: number | null;
  raison?: string | null;
  etat?: Etat | null;
  typeConge?: ITypeConge | null;
  salarie?: ISalarie | null;
}

export class DemandeConge implements IDemandeConge {
  constructor(
    public id?: number,
    public dateDebut?: dayjs.Dayjs | null,
    public dateFin?: dayjs.Dayjs | null,
    public duree?: number | null,
    public raison?: string | null,
    public etat?: Etat | null,
    public typeConge?: ITypeConge | null,
    public salarie?: ISalarie | null
  ) {}
}

export function getDemandeCongeIdentifier(demandeConge: IDemandeConge): number | undefined {
  return demandeConge.id;
}
