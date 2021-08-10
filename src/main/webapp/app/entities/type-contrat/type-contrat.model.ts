export interface ITypeContrat {
  id?: number;
  nom?: string | null;
}

export class TypeContrat implements ITypeContrat {
  constructor(public id?: number, public nom?: string | null) {}
}

export function getTypeContratIdentifier(typeContrat: ITypeContrat): number | undefined {
  return typeContrat.id;
}
