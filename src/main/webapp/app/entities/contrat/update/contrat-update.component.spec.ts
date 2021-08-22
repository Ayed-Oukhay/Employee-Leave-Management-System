jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContratService } from '../service/contrat.service';
import { IContrat, Contrat } from '../contrat.model';
import { ITypeContrat } from 'app/entities/type-contrat/type-contrat.model';
import { TypeContratService } from 'app/entities/type-contrat/service/type-contrat.service';
import { ISalarie } from 'app/entities/salarie/salarie.model';
import { SalarieService } from 'app/entities/salarie/service/salarie.service';

import { ContratUpdateComponent } from './contrat-update.component';

describe('Component Tests', () => {
  describe('Contrat Management Update Component', () => {
    let comp: ContratUpdateComponent;
    let fixture: ComponentFixture<ContratUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contratService: ContratService;
    let typeContratService: TypeContratService;
    let salarieService: SalarieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContratUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContratUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContratUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contratService = TestBed.inject(ContratService);
      typeContratService = TestBed.inject(TypeContratService);
      salarieService = TestBed.inject(SalarieService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call TypeContrat query and add missing value', () => {
        const contrat: IContrat = { id: 456 };
        const typeContrat: ITypeContrat = { id: 51775 };
        contrat.typeContrat = typeContrat;

        const typeContratCollection: ITypeContrat[] = [{ id: 46923 }];
        jest.spyOn(typeContratService, 'query').mockReturnValue(of(new HttpResponse({ body: typeContratCollection })));
        const additionalTypeContrats = [typeContrat];
        const expectedCollection: ITypeContrat[] = [...additionalTypeContrats, ...typeContratCollection];
        jest.spyOn(typeContratService, 'addTypeContratToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contrat });
        comp.ngOnInit();

        expect(typeContratService.query).toHaveBeenCalled();
        expect(typeContratService.addTypeContratToCollectionIfMissing).toHaveBeenCalledWith(
          typeContratCollection,
          ...additionalTypeContrats
        );
        expect(comp.typeContratsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Salarie query and add missing value', () => {
        const contrat: IContrat = { id: 456 };
        const salarie: ISalarie = { id: 97737 };
        contrat.salarie = salarie;

        const salarieCollection: ISalarie[] = [{ id: 83444 }];
        jest.spyOn(salarieService, 'query').mockReturnValue(of(new HttpResponse({ body: salarieCollection })));
        const additionalSalaries = [salarie];
        const expectedCollection: ISalarie[] = [...additionalSalaries, ...salarieCollection];
        jest.spyOn(salarieService, 'addSalarieToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contrat });
        comp.ngOnInit();

        expect(salarieService.query).toHaveBeenCalled();
        expect(salarieService.addSalarieToCollectionIfMissing).toHaveBeenCalledWith(salarieCollection, ...additionalSalaries);
        expect(comp.salariesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contrat: IContrat = { id: 456 };
        const typeContrat: ITypeContrat = { id: 44447 };
        contrat.typeContrat = typeContrat;
        const salarie: ISalarie = { id: 33420 };
        contrat.salarie = salarie;

        activatedRoute.data = of({ contrat });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contrat));
        expect(comp.typeContratsSharedCollection).toContain(typeContrat);
        expect(comp.salariesSharedCollection).toContain(salarie);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Contrat>>();
        const contrat = { id: 123 };
        jest.spyOn(contratService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contrat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contrat }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contratService.update).toHaveBeenCalledWith(contrat);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Contrat>>();
        const contrat = new Contrat();
        jest.spyOn(contratService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contrat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contrat }));
        saveSubject.complete();

        // THEN
        expect(contratService.create).toHaveBeenCalledWith(contrat);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Contrat>>();
        const contrat = { id: 123 };
        jest.spyOn(contratService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contrat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contratService.update).toHaveBeenCalledWith(contrat);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTypeContratById', () => {
        it('Should return tracked TypeContrat primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTypeContratById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSalarieById', () => {
        it('Should return tracked Salarie primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSalarieById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
