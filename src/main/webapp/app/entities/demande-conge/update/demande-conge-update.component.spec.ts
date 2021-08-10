jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DemandeCongeService } from '../service/demande-conge.service';
import { IDemandeConge, DemandeConge } from '../demande-conge.model';
import { ITypeConge } from 'app/entities/type-conge/type-conge.model';
import { TypeCongeService } from 'app/entities/type-conge/service/type-conge.service';
import { ISalarie } from 'app/entities/salarie/salarie.model';
import { SalarieService } from 'app/entities/salarie/service/salarie.service';

import { DemandeCongeUpdateComponent } from './demande-conge-update.component';

describe('Component Tests', () => {
  describe('DemandeConge Management Update Component', () => {
    let comp: DemandeCongeUpdateComponent;
    let fixture: ComponentFixture<DemandeCongeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let demandeCongeService: DemandeCongeService;
    let typeCongeService: TypeCongeService;
    let salarieService: SalarieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DemandeCongeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DemandeCongeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DemandeCongeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      demandeCongeService = TestBed.inject(DemandeCongeService);
      typeCongeService = TestBed.inject(TypeCongeService);
      salarieService = TestBed.inject(SalarieService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call TypeConge query and add missing value', () => {
        const demandeConge: IDemandeConge = { id: 456 };
        const typeConge: ITypeConge = { id: 50084 };
        demandeConge.typeConge = typeConge;

        const typeCongeCollection: ITypeConge[] = [{ id: 47820 }];
        jest.spyOn(typeCongeService, 'query').mockReturnValue(of(new HttpResponse({ body: typeCongeCollection })));
        const additionalTypeConges = [typeConge];
        const expectedCollection: ITypeConge[] = [...additionalTypeConges, ...typeCongeCollection];
        jest.spyOn(typeCongeService, 'addTypeCongeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ demandeConge });
        comp.ngOnInit();

        expect(typeCongeService.query).toHaveBeenCalled();
        expect(typeCongeService.addTypeCongeToCollectionIfMissing).toHaveBeenCalledWith(typeCongeCollection, ...additionalTypeConges);
        expect(comp.typeCongesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Salarie query and add missing value', () => {
        const demandeConge: IDemandeConge = { id: 456 };
        const salarie: ISalarie = { id: 35415 };
        demandeConge.salarie = salarie;

        const salarieCollection: ISalarie[] = [{ id: 6764 }];
        jest.spyOn(salarieService, 'query').mockReturnValue(of(new HttpResponse({ body: salarieCollection })));
        const additionalSalaries = [salarie];
        const expectedCollection: ISalarie[] = [...additionalSalaries, ...salarieCollection];
        jest.spyOn(salarieService, 'addSalarieToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ demandeConge });
        comp.ngOnInit();

        expect(salarieService.query).toHaveBeenCalled();
        expect(salarieService.addSalarieToCollectionIfMissing).toHaveBeenCalledWith(salarieCollection, ...additionalSalaries);
        expect(comp.salariesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const demandeConge: IDemandeConge = { id: 456 };
        const typeConge: ITypeConge = { id: 9987 };
        demandeConge.typeConge = typeConge;
        const salarie: ISalarie = { id: 64274 };
        demandeConge.salarie = salarie;

        activatedRoute.data = of({ demandeConge });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(demandeConge));
        expect(comp.typeCongesSharedCollection).toContain(typeConge);
        expect(comp.salariesSharedCollection).toContain(salarie);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DemandeConge>>();
        const demandeConge = { id: 123 };
        jest.spyOn(demandeCongeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ demandeConge });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: demandeConge }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(demandeCongeService.update).toHaveBeenCalledWith(demandeConge);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DemandeConge>>();
        const demandeConge = new DemandeConge();
        jest.spyOn(demandeCongeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ demandeConge });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: demandeConge }));
        saveSubject.complete();

        // THEN
        expect(demandeCongeService.create).toHaveBeenCalledWith(demandeConge);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DemandeConge>>();
        const demandeConge = { id: 123 };
        jest.spyOn(demandeCongeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ demandeConge });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(demandeCongeService.update).toHaveBeenCalledWith(demandeConge);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTypeCongeById', () => {
        it('Should return tracked TypeConge primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTypeCongeById(0, entity);
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
