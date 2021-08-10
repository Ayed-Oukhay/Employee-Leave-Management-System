import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SalarieDetailComponent } from './salarie-detail.component';

describe('Component Tests', () => {
  describe('Salarie Management Detail Component', () => {
    let comp: SalarieDetailComponent;
    let fixture: ComponentFixture<SalarieDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SalarieDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ salarie: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SalarieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SalarieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load salarie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.salarie).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
