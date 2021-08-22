import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalarie } from '../salarie.model';

@Component({
  selector: 'jhi-salarie-detail',
  templateUrl: './salarie-detail.component.html',
})
export class SalarieDetailComponent implements OnInit {
  salarie: ISalarie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salarie }) => {
      this.salarie = salarie;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
