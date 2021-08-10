import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeContrat, getTypeContratIdentifier } from '../type-contrat.model';

export type EntityResponseType = HttpResponse<ITypeContrat>;
export type EntityArrayResponseType = HttpResponse<ITypeContrat[]>;

@Injectable({ providedIn: 'root' })
export class TypeContratService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-contrats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typeContrat: ITypeContrat): Observable<EntityResponseType> {
    return this.http.post<ITypeContrat>(this.resourceUrl, typeContrat, { observe: 'response' });
  }

  update(typeContrat: ITypeContrat): Observable<EntityResponseType> {
    return this.http.put<ITypeContrat>(`${this.resourceUrl}/${getTypeContratIdentifier(typeContrat) as number}`, typeContrat, {
      observe: 'response',
    });
  }

  partialUpdate(typeContrat: ITypeContrat): Observable<EntityResponseType> {
    return this.http.patch<ITypeContrat>(`${this.resourceUrl}/${getTypeContratIdentifier(typeContrat) as number}`, typeContrat, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeContrat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeContrat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypeContratToCollectionIfMissing(
    typeContratCollection: ITypeContrat[],
    ...typeContratsToCheck: (ITypeContrat | null | undefined)[]
  ): ITypeContrat[] {
    const typeContrats: ITypeContrat[] = typeContratsToCheck.filter(isPresent);
    if (typeContrats.length > 0) {
      const typeContratCollectionIdentifiers = typeContratCollection.map(typeContratItem => getTypeContratIdentifier(typeContratItem)!);
      const typeContratsToAdd = typeContrats.filter(typeContratItem => {
        const typeContratIdentifier = getTypeContratIdentifier(typeContratItem);
        if (typeContratIdentifier == null || typeContratCollectionIdentifiers.includes(typeContratIdentifier)) {
          return false;
        }
        typeContratCollectionIdentifiers.push(typeContratIdentifier);
        return true;
      });
      return [...typeContratsToAdd, ...typeContratCollection];
    }
    return typeContratCollection;
  }
}
