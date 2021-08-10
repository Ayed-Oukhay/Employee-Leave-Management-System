export interface IPoste {
  id?: number;
  nom?: string | null;
  description?: string | null;
}

export class Poste implements IPoste {
  constructor(public id?: number, public nom?: string | null, public description?: string | null) {}
}

export function getPosteIdentifier(poste: IPoste): number | undefined {
  return poste.id;
}
