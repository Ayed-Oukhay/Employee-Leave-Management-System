jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITypeConge, TypeConge } from '../type-conge.model';
import { TypeCongeService } from '../service/type-conge.service';

import { TypeCongeRoutingResolveService } from './type-conge-routing-resolve.service';

describe('Service Tests', () => {
  describe('TypeConge routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TypeCongeRoutingResolveService;
    let service: TypeCongeService;
    let resultTypeConge: ITypeConge | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TypeCongeRoutingResolveService);
      service = TestBed.inject(TypeCongeService);
      resultTypeConge = undefined;
    });

    describe('resolve', () => {
      it('should return ITypeConge returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTypeConge = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTypeConge).toEqual({ id: 123 });
      });

      it('should return new ITypeConge if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTypeConge = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTypeConge).toEqual(new TypeConge());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TypeConge })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTypeConge = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTypeConge).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
