export interface IDepartement {
  id?: number;
  nom?: string | null;
}

export class Departement implements IDepartement {
  constructor(public id?: number, public nom?: string | null) {}
}

export function getDepartementIdentifier(departement: IDepartement): number | undefined {
  return departement.id;
}
