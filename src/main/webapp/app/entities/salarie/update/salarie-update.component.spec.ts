jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SalarieService } from '../service/salarie.service';
import { ISalarie, Salarie } from '../salarie.model';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';
import { IPoste } from 'app/entities/poste/poste.model';
import { PosteService } from 'app/entities/poste/service/poste.service';

import { SalarieUpdateComponent } from './salarie-update.component';

describe('Component Tests', () => {
  describe('Salarie Management Update Component', () => {
    let comp: SalarieUpdateComponent;
    let fixture: ComponentFixture<SalarieUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let salarieService: SalarieService;
    let departementService: DepartementService;
    let posteService: PosteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SalarieUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SalarieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SalarieUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      salarieService = TestBed.inject(SalarieService);
      departementService = TestBed.inject(DepartementService);
      posteService = TestBed.inject(PosteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Departement query and add missing value', () => {
        const salarie: ISalarie = { id: 456 };
        const departement: IDepartement = { id: 38307 };
        salarie.departement = departement;

        const departementCollection: IDepartement[] = [{ id: 69268 }];
        jest.spyOn(departementService, 'query').mockReturnValue(of(new HttpResponse({ body: departementCollection })));
        const additionalDepartements = [departement];
        const expectedCollection: IDepartement[] = [...additionalDepartements, ...departementCollection];
        jest.spyOn(departementService, 'addDepartementToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ salarie });
        comp.ngOnInit();

        expect(departementService.query).toHaveBeenCalled();
        expect(departementService.addDepartementToCollectionIfMissing).toHaveBeenCalledWith(
          departementCollection,
          ...additionalDepartements
        );
        expect(comp.departementsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Poste query and add missing value', () => {
        const salarie: ISalarie = { id: 456 };
        const poste: IPoste = { id: 81289 };
        salarie.poste = poste;

        const posteCollection: IPoste[] = [{ id: 72990 }];
        jest.spyOn(posteService, 'query').mockReturnValue(of(new HttpResponse({ body: posteCollection })));
        const additionalPostes = [poste];
        const expectedCollection: IPoste[] = [...additionalPostes, ...posteCollection];
        jest.spyOn(posteService, 'addPosteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ salarie });
        comp.ngOnInit();

        expect(posteService.query).toHaveBeenCalled();
        expect(posteService.addPosteToCollectionIfMissing).toHaveBeenCalledWith(posteCollection, ...additionalPostes);
        expect(comp.postesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const salarie: ISalarie = { id: 456 };
        const departement: IDepartement = { id: 3189 };
        salarie.departement = departement;
        const poste: IPoste = { id: 75648 };
        salarie.poste = poste;

        activatedRoute.data = of({ salarie });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(salarie));
        expect(comp.departementsSharedCollection).toContain(departement);
        expect(comp.postesSharedCollection).toContain(poste);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Salarie>>();
        const salarie = { id: 123 };
        jest.spyOn(salarieService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ salarie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: salarie }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(salarieService.update).toHaveBeenCalledWith(salarie);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Salarie>>();
        const salarie = new Salarie();
        jest.spyOn(salarieService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ salarie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: salarie }));
        saveSubject.complete();

        // THEN
        expect(salarieService.create).toHaveBeenCalledWith(salarie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Salarie>>();
        const salarie = { id: 123 };
        jest.spyOn(salarieService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ salarie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(salarieService.update).toHaveBeenCalledWith(salarie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackDepartementById', () => {
        it('Should return tracked Departement primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDepartementById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPosteById', () => {
        it('Should return tracked Poste primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPosteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
