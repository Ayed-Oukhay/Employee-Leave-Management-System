import * as dayjs from 'dayjs';
import { IDemandeConge } from 'app/entities/demande-conge/demande-conge.model';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { IDepartement } from 'app/entities/departement/departement.model';
import { IPoste } from 'app/entities/poste/poste.model';
import { Role } from 'app/entities/enumerations/role.model';

export interface ISalarie {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  login?: string | null;
  email?: string | null;
  manager?: string | null;
  role?: Role | null;
  actif?: boolean | null;
  dateDebut?: dayjs.Dayjs | null;
  iDEntreprise?: number | null;
  lDAPPath?: string | null;
  demandeConges?: IDemandeConge[] | null;
  contrats?: IContrat[] | null;
  departement?: IDepartement | null;
  poste?: IPoste | null;
}

export class Salarie implements ISalarie {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public login?: string | null,
    public email?: string | null,
    public manager?: string | null,
    public role?: Role | null,
    public actif?: boolean | null,
    public dateDebut?: dayjs.Dayjs | null,
    public iDEntreprise?: number | null,
    public lDAPPath?: string | null,
    public demandeConges?: IDemandeConge[] | null,
    public contrats?: IContrat[] | null,
    public departement?: IDepartement | null,
    public poste?: IPoste | null
  ) {
    this.actif = this.actif ?? false;
  }
}

export function getSalarieIdentifier(salarie: ISalarie): number | undefined {
  return salarie.id;
}
