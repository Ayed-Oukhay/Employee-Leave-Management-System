import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDemandeConge } from '../demande-conge.model';

@Component({
  selector: 'jhi-demande-conge-detail',
  templateUrl: './demande-conge-detail.component.html',
})
export class DemandeCongeDetailComponent implements OnInit {
  demandeConge: IDemandeConge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeConge }) => {
      this.demandeConge = demandeConge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
