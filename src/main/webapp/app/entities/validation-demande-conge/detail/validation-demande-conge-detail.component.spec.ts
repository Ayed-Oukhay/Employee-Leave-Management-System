import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DemandeCongeDetailComponent } from './validation-demande-conge-detail.component';

describe('Component Tests', () => {
  describe('DemandeConge Management Detail Component', () => {
    let comp: DemandeCongeDetailComponent;
    let fixture: ComponentFixture<DemandeCongeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DemandeCongeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ demandeConge: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DemandeCongeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DemandeCongeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load demandeConge on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.demandeConge).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
