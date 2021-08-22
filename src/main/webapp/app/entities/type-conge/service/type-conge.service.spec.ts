import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeConge, TypeConge } from '../type-conge.model';

import { TypeCongeService } from './type-conge.service';

describe('Service Tests', () => {
  describe('TypeConge Service', () => {
    let service: TypeCongeService;
    let httpMock: HttpTestingController;
    let elemDefault: ITypeConge;
    let expectedResult: ITypeConge | ITypeConge[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TypeCongeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TypeConge', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TypeConge()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TypeConge', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TypeConge', () => {
        const patchObject = Object.assign(
          {
            nom: 'BBBBBB',
          },
          new TypeConge()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TypeConge', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TypeConge', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTypeCongeToCollectionIfMissing', () => {
        it('should add a TypeConge to an empty array', () => {
          const typeConge: ITypeConge = { id: 123 };
          expectedResult = service.addTypeCongeToCollectionIfMissing([], typeConge);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(typeConge);
        });

        it('should not add a TypeConge to an array that contains it', () => {
          const typeConge: ITypeConge = { id: 123 };
          const typeCongeCollection: ITypeConge[] = [
            {
              ...typeConge,
            },
            { id: 456 },
          ];
          expectedResult = service.addTypeCongeToCollectionIfMissing(typeCongeCollection, typeConge);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TypeConge to an array that doesn't contain it", () => {
          const typeConge: ITypeConge = { id: 123 };
          const typeCongeCollection: ITypeConge[] = [{ id: 456 }];
          expectedResult = service.addTypeCongeToCollectionIfMissing(typeCongeCollection, typeConge);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(typeConge);
        });

        it('should add only unique TypeConge to an array', () => {
          const typeCongeArray: ITypeConge[] = [{ id: 123 }, { id: 456 }, { id: 47399 }];
          const typeCongeCollection: ITypeConge[] = [{ id: 123 }];
          expectedResult = service.addTypeCongeToCollectionIfMissing(typeCongeCollection, ...typeCongeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const typeConge: ITypeConge = { id: 123 };
          const typeConge2: ITypeConge = { id: 456 };
          expectedResult = service.addTypeCongeToCollectionIfMissing([], typeConge, typeConge2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(typeConge);
          expect(expectedResult).toContain(typeConge2);
        });

        it('should accept null and undefined values', () => {
          const typeConge: ITypeConge = { id: 123 };
          expectedResult = service.addTypeCongeToCollectionIfMissing([], null, typeConge, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(typeConge);
        });

        it('should return initial array if no TypeConge is added', () => {
          const typeCongeCollection: ITypeConge[] = [{ id: 123 }];
          expectedResult = service.addTypeCongeToCollectionIfMissing(typeCongeCollection, undefined, null);
          expect(expectedResult).toEqual(typeCongeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
