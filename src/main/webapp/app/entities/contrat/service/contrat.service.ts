import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContrat, getContratIdentifier } from '../contrat.model';

export type EntityResponseType = HttpResponse<IContrat>;
export type EntityArrayResponseType = HttpResponse<IContrat[]>;

@Injectable({ providedIn: 'root' })
export class ContratService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contrats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contrat: IContrat): Observable<EntityResponseType> {
    return this.http.post<IContrat>(this.resourceUrl, contrat, { observe: 'response' });
  }

  update(contrat: IContrat): Observable<EntityResponseType> {
    return this.http.put<IContrat>(`${this.resourceUrl}/${getContratIdentifier(contrat) as number}`, contrat, { observe: 'response' });
  }

  partialUpdate(contrat: IContrat): Observable<EntityResponseType> {
    return this.http.patch<IContrat>(`${this.resourceUrl}/${getContratIdentifier(contrat) as number}`, contrat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContrat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContrat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContratToCollectionIfMissing(contratCollection: IContrat[], ...contratsToCheck: (IContrat | null | undefined)[]): IContrat[] {
    const contrats: IContrat[] = contratsToCheck.filter(isPresent);
    if (contrats.length > 0) {
      const contratCollectionIdentifiers = contratCollection.map(contratItem => getContratIdentifier(contratItem)!);
      const contratsToAdd = contrats.filter(contratItem => {
        const contratIdentifier = getContratIdentifier(contratItem);
        if (contratIdentifier == null || contratCollectionIdentifiers.includes(contratIdentifier)) {
          return false;
        }
        contratCollectionIdentifiers.push(contratIdentifier);
        return true;
      });
      return [...contratsToAdd, ...contratCollection];
    }
    return contratCollection;
  }
}
