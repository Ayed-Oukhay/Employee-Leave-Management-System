jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISalarie, Salarie } from '../salarie.model';
import { SalarieService } from '../service/salarie.service';

import { SalarieRoutingResolveService } from './salarie-routing-resolve.service';

describe('Service Tests', () => {
  describe('Salarie routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SalarieRoutingResolveService;
    let service: SalarieService;
    let resultSalarie: ISalarie | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SalarieRoutingResolveService);
      service = TestBed.inject(SalarieService);
      resultSalarie = undefined;
    });

    describe('resolve', () => {
      it('should return ISalarie returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSalarie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSalarie).toEqual({ id: 123 });
      });

      it('should return new ISalarie if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSalarie = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSalarie).toEqual(new Salarie());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Salarie })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSalarie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSalarie).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
