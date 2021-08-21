import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Etat } from 'app/entities/enumerations/etat.model';
import { IDemandeConge, DemandeConge } from '../validation-demande-conge.model';

import { DemandeCongeService } from './validation-demande-conge.service';

describe('Service Tests', () => {
  describe('DemandeConge Service', () => {
    let service: DemandeCongeService;
    let httpMock: HttpTestingController;
    let elemDefault: IDemandeConge;
    let expectedResult: IDemandeConge | IDemandeConge[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DemandeCongeService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dateDebut: currentDate,
        dateFin: currentDate,
        duree: 0,
        raison: 'AAAAAAA',
        etat: Etat.Accepte,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateDebut: currentDate.format(DATE_FORMAT),
            dateFin: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a DemandeConge', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateDebut: currentDate.format(DATE_FORMAT),
            dateFin: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDebut: currentDate,
            dateFin: currentDate,
          },
          returnedFromService
        );

        service.create(new DemandeConge()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DemandeConge', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateDebut: currentDate.format(DATE_FORMAT),
            dateFin: currentDate.format(DATE_FORMAT),
            duree: 1,
            raison: 'BBBBBB',
            etat: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDebut: currentDate,
            dateFin: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DemandeConge', () => {
        const patchObject = Object.assign(
          {
            dateDebut: currentDate.format(DATE_FORMAT),
          },
          new DemandeConge()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateDebut: currentDate,
            dateFin: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DemandeConge', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateDebut: currentDate.format(DATE_FORMAT),
            dateFin: currentDate.format(DATE_FORMAT),
            duree: 1,
            raison: 'BBBBBB',
            etat: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDebut: currentDate,
            dateFin: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a DemandeConge', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDemandeCongeToCollectionIfMissing', () => {
        it('should add a DemandeConge to an empty array', () => {
          const demandeConge: IDemandeConge = { id: 123 };
          expectedResult = service.addDemandeCongeToCollectionIfMissing([], demandeConge);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(demandeConge);
        });

        it('should not add a DemandeConge to an array that contains it', () => {
          const demandeConge: IDemandeConge = { id: 123 };
          const demandeCongeCollection: IDemandeConge[] = [
            {
              ...demandeConge,
            },
            { id: 456 },
          ];
          expectedResult = service.addDemandeCongeToCollectionIfMissing(demandeCongeCollection, demandeConge);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DemandeConge to an array that doesn't contain it", () => {
          const demandeConge: IDemandeConge = { id: 123 };
          const demandeCongeCollection: IDemandeConge[] = [{ id: 456 }];
          expectedResult = service.addDemandeCongeToCollectionIfMissing(demandeCongeCollection, demandeConge);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(demandeConge);
        });

        it('should add only unique DemandeConge to an array', () => {
          const demandeCongeArray: IDemandeConge[] = [{ id: 123 }, { id: 456 }, { id: 56740 }];
          const demandeCongeCollection: IDemandeConge[] = [{ id: 123 }];
          expectedResult = service.addDemandeCongeToCollectionIfMissing(demandeCongeCollection, ...demandeCongeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const demandeConge: IDemandeConge = { id: 123 };
          const demandeConge2: IDemandeConge = { id: 456 };
          expectedResult = service.addDemandeCongeToCollectionIfMissing([], demandeConge, demandeConge2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(demandeConge);
          expect(expectedResult).toContain(demandeConge2);
        });

        it('should accept null and undefined values', () => {
          const demandeConge: IDemandeConge = { id: 123 };
          expectedResult = service.addDemandeCongeToCollectionIfMissing([], null, demandeConge, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(demandeConge);
        });

        it('should return initial array if no DemandeConge is added', () => {
          const demandeCongeCollection: IDemandeConge[] = [{ id: 123 }];
          expectedResult = service.addDemandeCongeToCollectionIfMissing(demandeCongeCollection, undefined, null);
          expect(expectedResult).toEqual(demandeCongeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
