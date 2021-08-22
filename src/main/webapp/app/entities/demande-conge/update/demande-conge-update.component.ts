import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDemandeConge, DemandeConge } from '../demande-conge.model';
import { DemandeCongeService } from '../service/demande-conge.service';
import { ITypeConge } from 'app/entities/type-conge/type-conge.model';
import { TypeCongeService } from 'app/entities/type-conge/service/type-conge.service';
import { ISalarie } from 'app/entities/salarie/salarie.model';
import { SalarieService } from 'app/entities/salarie/service/salarie.service';

@Component({
  selector: 'jhi-demande-conge-update',
  templateUrl: './demande-conge-update.component.html',
})
export class DemandeCongeUpdateComponent implements OnInit {
  isSaving = false;
  dateDebutType = 'text';
  dateFinType = 'text';
  duree = '';
  // submitted = false;
  typeCongesSharedCollection: ITypeConge[] = [];
  salariesSharedCollection: ISalarie[] = [];

  editForm = this.fb.group({
    id: [],
    dateDebut: [],
    dateFin: [],
    duree: [],
    raison: [],
    etat: [],
    typeConge: null,
    salarie: [],
  });

  constructor(
    protected demandeCongeService: DemandeCongeService,
    protected typeCongeService: TypeCongeService,
    protected salarieService: SalarieService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeConge }) => {
      this.updateForm(demandeConge);

      this.loadRelationshipsOptions();
    });

    this.editForm = this.fb.group({
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required],
      raison: ['', Validators.required],
      Etat: [false, Validators.requiredTrue],
    });
  }

  get f(): any {
    return this.editForm.controls;
  }

  // onSubmit():void {
  //   this.submitted = true;

  //   // stop here if form is invalid
  //   if (this.editForm.invalid) {
  //       return;
  //   }
  // }

  onReset(): void {
    this.isSaving = false;
    this.editForm.reset();
  }

  calculDuree(): void {
    const debut = this.editForm.controls['dateDebut'].value;
    const fin = this.editForm.controls['dateFin'].value;
    // console.log('debut = ', debut);
    // console.log('fin = ', fin);
    if (debut !== null && fin !== null) {
      const Difference_In_Time = new Date(fin).getTime() - new Date(debut).getTime();
      this.duree = (Difference_In_Time / (1000 * 3600 * 24)).toString();
      // console.log('duree = ', this.duree);
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (this.editForm.valid) {
      const demandeConge = this.createFromForm();
      if (demandeConge.id !== undefined) {
        this.subscribeToSaveResponse(this.demandeCongeService.update(demandeConge));
      } else {
        this.subscribeToSaveResponse(this.demandeCongeService.create(demandeConge));
      }
    }
  }

  trackTypeCongeById(index: number, item: ITypeConge): number {
    return item.id!;
  }

  trackSalarieById(index: number, item: ISalarie): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemandeConge>>): void {
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

  protected updateForm(demandeConge: IDemandeConge): void {
    this.editForm.patchValue({
      id: demandeConge.id,
      dateDebut: demandeConge.dateDebut,
      dateFin: demandeConge.dateFin,
      duree: demandeConge.duree,
      raison: demandeConge.raison,
      etat: demandeConge.etat,
      typeConge: demandeConge.typeConge,
      salarie: demandeConge.salarie,
    });

    this.typeCongesSharedCollection = this.typeCongeService.addTypeCongeToCollectionIfMissing(
      this.typeCongesSharedCollection,
      demandeConge.typeConge
    );
    this.salariesSharedCollection = this.salarieService.addSalarieToCollectionIfMissing(
      this.salariesSharedCollection,
      demandeConge.salarie
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeCongeService
      .query()
      .pipe(map((res: HttpResponse<ITypeConge[]>) => res.body ?? []))
      .pipe(
        map((typeConges: ITypeConge[]) =>
          this.typeCongeService.addTypeCongeToCollectionIfMissing(typeConges, this.editForm.get('typeConge')!.value)
        )
      )
      .subscribe((typeConges: ITypeConge[]) => (this.typeCongesSharedCollection = typeConges));

    this.salarieService
      .query()
      .pipe(map((res: HttpResponse<ISalarie[]>) => res.body ?? []))
      .pipe(
        map((salaries: ISalarie[]) => this.salarieService.addSalarieToCollectionIfMissing(salaries, this.editForm.get('salarie')!.value))
      )
      .subscribe((salaries: ISalarie[]) => (this.salariesSharedCollection = salaries));
  }

  protected createFromForm(): IDemandeConge {
    return {
      ...new DemandeConge(),
      id: this.editForm.get(['id'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value,
      dateFin: this.editForm.get(['dateFin'])!.value,
      duree: this.editForm.get(['duree'])!.value,
      raison: this.editForm.get(['raison'])!.value,
      etat: this.editForm.get(['etat'])!.value,
      typeConge: this.editForm.get(['typeConge'])!.value,
      salarie: this.editForm.get(['salarie'])!.value,
    };
  }
}
