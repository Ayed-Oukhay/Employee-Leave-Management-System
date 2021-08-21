jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDemandeConge, DemandeConge } from '../validation-demande-conge.model';
import { DemandeCongeService } from '../service/validation-demande-conge.service';

import { DemandeCongeRoutingResolveService } from './validation-demande-conge-routing-resolve.service';

describe('Service Tests', () => {
  describe('DemandeConge routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DemandeCongeRoutingResolveService;
    let service: DemandeCongeService;
    let resultDemandeConge: IDemandeConge | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DemandeCongeRoutingResolveService);
      service = TestBed.inject(DemandeCongeService);
      resultDemandeConge = undefined;
    });

    describe('resolve', () => {
      it('should return IDemandeConge returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemandeConge = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDemandeConge).toEqual({ id: 123 });
      });

      it('should return new IDemandeConge if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemandeConge = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDemandeConge).toEqual(new DemandeConge());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DemandeConge })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemandeConge = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDemandeConge).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
