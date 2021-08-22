jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TypeCongeService } from '../service/type-conge.service';
import { ITypeConge, TypeConge } from '../type-conge.model';

import { TypeCongeUpdateComponent } from './type-conge-update.component';

describe('Component Tests', () => {
  describe('TypeConge Management Update Component', () => {
    let comp: TypeCongeUpdateComponent;
    let fixture: ComponentFixture<TypeCongeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let typeCongeService: TypeCongeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TypeCongeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TypeCongeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TypeCongeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      typeCongeService = TestBed.inject(TypeCongeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const typeConge: ITypeConge = { id: 456 };

        activatedRoute.data = of({ typeConge });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(typeConge));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TypeConge>>();
        const typeConge = { id: 123 };
        jest.spyOn(typeCongeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeConge });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: typeConge }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(typeCongeService.update).toHaveBeenCalledWith(typeConge);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TypeConge>>();
        const typeConge = new TypeConge();
        jest.spyOn(typeCongeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeConge });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: typeConge }));
        saveSubject.complete();

        // THEN
        expect(typeCongeService.create).toHaveBeenCalledWith(typeConge);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TypeConge>>();
        const typeConge = { id: 123 };
        jest.spyOn(typeCongeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeConge });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(typeCongeService.update).toHaveBeenCalledWith(typeConge);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
