export interface ITypeConge {
  id?: number;
  nom?: string | null;
}

export class TypeConge implements ITypeConge {
  constructor(public id?: number, public nom?: string | null) {}
}

export function getTypeCongeIdentifier(typeConge: ITypeConge): number | undefined {
  return typeConge.id;
}
