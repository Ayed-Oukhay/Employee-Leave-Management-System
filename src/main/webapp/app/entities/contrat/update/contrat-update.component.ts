import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContrat, Contrat } from '../contrat.model';
import { ContratService } from '../service/contrat.service';
import { ITypeContrat } from 'app/entities/type-contrat/type-contrat.model';
import { TypeContratService } from 'app/entities/type-contrat/service/type-contrat.service';
import { ISalarie } from 'app/entities/salarie/salarie.model';
import { SalarieService } from 'app/entities/salarie/service/salarie.service';

@Component({
  selector: 'jhi-contrat-update',
  templateUrl: './contrat-update.component.html',
})
export class ContratUpdateComponent implements OnInit {
  isSaving = false;

  typeContratsSharedCollection: ITypeContrat[] = [];
  salariesSharedCollection: ISalarie[] = [];

  editForm = this.fb.group({
    id: [],
    typeContrat: [],
    salarie: [],
  });

  constructor(
    protected contratService: ContratService,
    protected typeContratService: TypeContratService,
    protected salarieService: SalarieService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contrat }) => {
      this.updateForm(contrat);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contrat = this.createFromForm();
    if (contrat.id !== undefined) {
      this.subscribeToSaveResponse(this.contratService.update(contrat));
    } else {
      this.subscribeToSaveResponse(this.contratService.create(contrat));
    }
  }

  trackTypeContratById(index: number, item: ITypeContrat): number {
    return item.id!;
  }

  trackSalarieById(index: number, item: ISalarie): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContrat>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contrat: IContrat): void {
    this.editForm.patchValue({
      id: contrat.id,
      typeContrat: contrat.typeContrat,
      salarie: contrat.salarie,
    });

    this.typeContratsSharedCollection = this.typeContratService.addTypeContratToCollectionIfMissing(
      this.typeContratsSharedCollection,
      contrat.typeContrat
    );
    this.salariesSharedCollection = this.salarieService.addSalarieToCollectionIfMissing(this.salariesSharedCollection, contrat.salarie);
  }

  protected loadRelationshipsOptions(): void {
    this.typeContratService
      .query()
      .pipe(map((res: HttpResponse<ITypeContrat[]>) => res.body ?? []))
      .pipe(
        map((typeContrats: ITypeContrat[]) =>
          this.typeContratService.addTypeContratToCollectionIfMissing(typeContrats, this.editForm.get('typeContrat')!.value)
        )
      )
      .subscribe((typeContrats: ITypeContrat[]) => (this.typeContratsSharedCollection = typeContrats));

    this.salarieService
      .query()
      .pipe(map((res: HttpResponse<ISalarie[]>) => res.body ?? []))
      .pipe(
        map((salaries: ISalarie[]) => this.salarieService.addSalarieToCollectionIfMissing(salaries, this.editForm.get('salarie')!.value))
      )
      .subscribe((salaries: ISalarie[]) => (this.salariesSharedCollection = salaries));
  }

  protected createFromForm(): IContrat {
    return {
      ...new Contrat(),
      id: this.editForm.get(['id'])!.value,
      typeContrat: this.editForm.get(['typeContrat'])!.value,
      salarie: this.editForm.get(['salarie'])!.value,
    };
  }
}
