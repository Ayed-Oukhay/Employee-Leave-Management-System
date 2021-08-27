import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';
import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISalarie, getSalarieIdentifier } from '../salarie.model';

export type EntityResponseType = HttpResponse<ISalarie>;
export type EntityArrayResponseType = HttpResponse<ISalarie[]>;

@Injectable({ providedIn: 'root' })
export class SalarieService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/salaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(salarie: ISalarie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salarie);
    return this.http
      .post<ISalarie>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(salarie: ISalarie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salarie);
    return this.http
      .put<ISalarie>(`${this.resourceUrl}/${getSalarieIdentifier(salarie) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(salarie: ISalarie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salarie);
    return this.http
      .patch<ISalarie>(`${this.resourceUrl}/${getSalarieIdentifier(salarie) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISalarie>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISalarie[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSalarieToCollectionIfMissing(salarieCollection: ISalarie[], ...salariesToCheck: (ISalarie | null | undefined)[]): ISalarie[] {
    const salaries: ISalarie[] = salariesToCheck.filter(isPresent);
    if (salaries.length > 0) {
      const salarieCollectionIdentifiers = salarieCollection.map(salarieItem => getSalarieIdentifier(salarieItem)!);
      const salariesToAdd = salaries.filter(salarieItem => {
        const salarieIdentifier = getSalarieIdentifier(salarieItem);
        if (salarieIdentifier == null || salarieCollectionIdentifiers.includes(salarieIdentifier)) {
          return false;
        }
        salarieCollectionIdentifiers.push(salarieIdentifier);
        return true;
      });
      return [...salariesToAdd, ...salarieCollection];
    }
    return salarieCollection;
  }

  protected convertDateFromClient(salarie: ISalarie): ISalarie {
    return Object.assign({}, salarie, {
      dateDebut: salarie.dateDebut?.isValid() ? salarie.dateDebut.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDebut = res.body.dateDebut ? dayjs(res.body.dateDebut) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((salarie: ISalarie) => {
        salarie.dateDebut = salarie.dateDebut ? dayjs(salarie.dateDebut) : undefined;
      });
    }
    return res;
  }
}
