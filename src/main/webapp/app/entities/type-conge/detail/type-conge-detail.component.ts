import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeConge } from '../type-conge.model';

@Component({
  selector: 'jhi-type-conge-detail',
  templateUrl: './type-conge-detail.component.html',
})
export class TypeCongeDetailComponent implements OnInit {
  typeConge: ITypeConge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeConge }) => {
      this.typeConge = typeConge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
