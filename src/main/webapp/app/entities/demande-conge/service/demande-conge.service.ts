import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemandeConge, getDemandeCongeIdentifier } from '../demande-conge.model';

export type EntityResponseType = HttpResponse<IDemandeConge>;
export type EntityArrayResponseType = HttpResponse<IDemandeConge[]>;

@Injectable({ providedIn: 'root' })
export class DemandeCongeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demande-conges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(demandeConge: IDemandeConge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeConge);
    return this.http
      .post<IDemandeConge>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(demandeConge: IDemandeConge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeConge);
    return this.http
      .put<IDemandeConge>(`${this.resourceUrl}/${getDemandeCongeIdentifier(demandeConge) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(demandeConge: IDemandeConge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeConge);
    return this.http
      .patch<IDemandeConge>(`${this.resourceUrl}/${getDemandeCongeIdentifier(demandeConge) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDemandeConge>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDemandeConge[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemandeCongeToCollectionIfMissing(
    demandeCongeCollection: IDemandeConge[],
    ...demandeCongesToCheck: (IDemandeConge | null | undefined)[]
  ): IDemandeConge[] {
    const demandeConges: IDemandeConge[] = demandeCongesToCheck.filter(isPresent);
    if (demandeConges.length > 0) {
      const demandeCongeCollectionIdentifiers = demandeCongeCollection.map(
        demandeCongeItem => getDemandeCongeIdentifier(demandeCongeItem)!
      );
      const demandeCongesToAdd = demandeConges.filter(demandeCongeItem => {
        const demandeCongeIdentifier = getDemandeCongeIdentifier(demandeCongeItem);
        if (demandeCongeIdentifier == null || demandeCongeCollectionIdentifiers.includes(demandeCongeIdentifier)) {
          return false;
        }
        demandeCongeCollectionIdentifiers.push(demandeCongeIdentifier);
        return true;
      });
      return [...demandeCongesToAdd, ...demandeCongeCollection];
    }
    return demandeCongeCollection;
  }

  protected convertDateFromClient(demandeConge: IDemandeConge): IDemandeConge {
    return Object.assign({}, demandeConge, {
      dateDebut: demandeConge.dateDebut?.isValid() ? demandeConge.dateDebut.format(DATE_FORMAT) : undefined,
      dateFin: demandeConge.dateFin?.isValid() ? demandeConge.dateFin.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDebut = res.body.dateDebut ? dayjs(res.body.dateDebut) : undefined;
      res.body.dateFin = res.body.dateFin ? dayjs(res.body.dateFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((demandeConge: IDemandeConge) => {
        demandeConge.dateDebut = demandeConge.dateDebut ? dayjs(demandeConge.dateDebut) : undefined;
        demandeConge.dateFin = demandeConge.dateFin ? dayjs(demandeConge.dateFin) : undefined;
      });
    }
    return res;
  }

  /* protected Any<DemandeConge> findByManager(Pageable pageable) {
    log.debug("Request to get DemandeConges based on the connected HR Admin");
    List<DemandeConge> filteredRepo = demandeCongeRepository.findAll(pageable);
    for (DemandeConge d:demandeCongeRepository.findAll(pageable)){
        Long idS = d.getSalarie().getId();
        Salarie s = salarieRepository.findOne(idS);
        String manager = s.getManager();
        if (manager=="admin"){
            filteredRepo.add(d);
      }
    }
    return demandeCongeRepository.findByManager(pageable);
  } */

}
