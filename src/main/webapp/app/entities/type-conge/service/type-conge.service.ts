import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeConge, getTypeCongeIdentifier } from '../type-conge.model';

export type EntityResponseType = HttpResponse<ITypeConge>;
export type EntityArrayResponseType = HttpResponse<ITypeConge[]>;

@Injectable({ providedIn: 'root' })
export class TypeCongeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-conges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typeConge: ITypeConge): Observable<EntityResponseType> {
    return this.http.post<ITypeConge>(this.resourceUrl, typeConge, { observe: 'response' });
  }

  update(typeConge: ITypeConge): Observable<EntityResponseType> {
    return this.http.put<ITypeConge>(`${this.resourceUrl}/${getTypeCongeIdentifier(typeConge) as number}`, typeConge, {
      observe: 'response',
    });
  }

  partialUpdate(typeConge: ITypeConge): Observable<EntityResponseType> {
    return this.http.patch<ITypeConge>(`${this.resourceUrl}/${getTypeCongeIdentifier(typeConge) as number}`, typeConge, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeConge>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeConge[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypeCongeToCollectionIfMissing(
    typeCongeCollection: ITypeConge[],
    ...typeCongesToCheck: (ITypeConge | null | undefined)[]
  ): ITypeConge[] {
    const typeConges: ITypeConge[] = typeCongesToCheck.filter(isPresent);
    if (typeConges.length > 0) {
      const typeCongeCollectionIdentifiers = typeCongeCollection.map(typeCongeItem => getTypeCongeIdentifier(typeCongeItem)!);
      const typeCongesToAdd = typeConges.filter(typeCongeItem => {
        const typeCongeIdentifier = getTypeCongeIdentifier(typeCongeItem);
        if (typeCongeIdentifier == null || typeCongeCollectionIdentifiers.includes(typeCongeIdentifier)) {
          return false;
        }
        typeCongeCollectionIdentifiers.push(typeCongeIdentifier);
        return true;
      });
      return [...typeCongesToAdd, ...typeCongeCollection];
    }
    return typeCongeCollection;
  }
}
