import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Role } from 'app/entities/enumerations/role.model';
import { ISalarie, Salarie } from '../salarie.model';

import { SalarieService } from './salarie.service';

describe('Service Tests', () => {
  describe('Salarie Service', () => {
    let service: SalarieService;
    let httpMock: HttpTestingController;
    let elemDefault: ISalarie;
    let expectedResult: ISalarie | ISalarie[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SalarieService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
        prenom: 'AAAAAAA',
        login: 'AAAAAAA',
        email: 'AAAAAAA',
        manager: 'AAAAAAA',
        role: Role.ROLE_ADMIN,
        actif: false,
        dateDebut: currentDate,
        iDEntreprise: 0,
        lDAPPath: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateDebut: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Salarie', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateDebut: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDebut: currentDate,
          },
          returnedFromService
        );

        service.create(new Salarie()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Salarie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            login: 'BBBBBB',
            email: 'BBBBBB',
            manager: 'BBBBBB',
            role: 'BBBBBB',
            actif: true,
            dateDebut: currentDate.format(DATE_FORMAT),
            iDEntreprise: 1,
            lDAPPath: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDebut: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Salarie', () => {
        const patchObject = Object.assign(
          {
            nom: 'BBBBBB',
            email: 'BBBBBB',
            role: 'BBBBBB',
            actif: true,
          },
          new Salarie()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateDebut: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Salarie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            login: 'BBBBBB',
            email: 'BBBBBB',
            manager: 'BBBBBB',
            role: 'BBBBBB',
            actif: true,
            dateDebut: currentDate.format(DATE_FORMAT),
            iDEntreprise: 1,
            lDAPPath: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDebut: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Salarie', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSalarieToCollectionIfMissing', () => {
        it('should add a Salarie to an empty array', () => {
          const salarie: ISalarie = { id: 123 };
          expectedResult = service.addSalarieToCollectionIfMissing([], salarie);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(salarie);
        });

        it('should not add a Salarie to an array that contains it', () => {
          const salarie: ISalarie = { id: 123 };
          const salarieCollection: ISalarie[] = [
            {
              ...salarie,
            },
            { id: 456 },
          ];
          expectedResult = service.addSalarieToCollectionIfMissing(salarieCollection, salarie);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Salarie to an array that doesn't contain it", () => {
          const salarie: ISalarie = { id: 123 };
          const salarieCollection: ISalarie[] = [{ id: 456 }];
          expectedResult = service.addSalarieToCollectionIfMissing(salarieCollection, salarie);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(salarie);
        });

        it('should add only unique Salarie to an array', () => {
          const salarieArray: ISalarie[] = [{ id: 123 }, { id: 456 }, { id: 32297 }];
          const salarieCollection: ISalarie[] = [{ id: 123 }];
          expectedResult = service.addSalarieToCollectionIfMissing(salarieCollection, ...salarieArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const salarie: ISalarie = { id: 123 };
          const salarie2: ISalarie = { id: 456 };
          expectedResult = service.addSalarieToCollectionIfMissing([], salarie, salarie2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(salarie);
          expect(expectedResult).toContain(salarie2);
        });

        it('should accept null and undefined values', () => {
          const salarie: ISalarie = { id: 123 };
          expectedResult = service.addSalarieToCollectionIfMissing([], null, salarie, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(salarie);
        });

        it('should return initial array if no Salarie is added', () => {
          const salarieCollection: ISalarie[] = [{ id: 123 }];
          expectedResult = service.addSalarieToCollectionIfMissing(salarieCollection, undefined, null);
          expect(expectedResult).toEqual(salarieCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
