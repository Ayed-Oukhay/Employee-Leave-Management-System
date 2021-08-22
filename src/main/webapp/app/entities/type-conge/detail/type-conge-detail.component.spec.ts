import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypeCongeDetailComponent } from './type-conge-detail.component';

describe('Component Tests', () => {
  describe('TypeConge Management Detail Component', () => {
    let comp: TypeCongeDetailComponent;
    let fixture: ComponentFixture<TypeCongeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TypeCongeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ typeConge: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TypeCongeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypeCongeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load typeConge on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.typeConge).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
