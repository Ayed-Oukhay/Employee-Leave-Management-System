import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISalarie, Salarie } from '../salarie.model';
import { SalarieService } from '../service/salarie.service';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';
import { IPoste } from 'app/entities/poste/poste.model';
import { PosteService } from 'app/entities/poste/service/poste.service';

@Component({
  selector: 'jhi-salarie-update',
  templateUrl: './salarie-update.component.html',
})
export class SalarieUpdateComponent implements OnInit {
  isSaving = false;

  departementsSharedCollection: IDepartement[] = [];
  postesSharedCollection: IPoste[] = [];
  salariesSharedCollection: ISalarie[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    login: [],
    email: [],
    manager: [],
    role: [],
    actif: [],
    dateDebut: [],
    iDEntreprise: [],
    lDAPPath: [],
    departement: [],
    poste: [],
  });

  constructor(
    protected salarieService: SalarieService,
    protected departementService: DepartementService,
    protected posteService: PosteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salarie }) => {
      this.updateForm(salarie);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salarie = this.createFromForm();
    if (salarie.id !== undefined) {
      this.subscribeToSaveResponse(this.salarieService.update(salarie));
    } else {
      this.subscribeToSaveResponse(this.salarieService.create(salarie));
    }
  }

  trackDepartementById(index: number, item: IDepartement): number {
    return item.id!;
  }

  trackPosteById(index: number, item: IPoste): number {
    return item.id!;
  }

  trackSalarieById(index: number, item: ISalarie): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalarie>>): void {
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

  protected updateForm(salarie: ISalarie): void {
    this.editForm.patchValue({
      id: salarie.id,
      nom: salarie.nom,
      prenom: salarie.prenom,
      login: salarie.login,
      email: salarie.email,
      manager: salarie.manager,
      role: salarie.role,
      actif: salarie.actif,
      dateDebut: salarie.dateDebut,
      iDEntreprise: salarie.iDEntreprise,
      lDAPPath: salarie.lDAPPath,
      departement: salarie.departement,
      poste: salarie.poste,
    });

    this.salariesSharedCollection = this.salarieService.addSalarieToCollectionIfMissing(
      this.salariesSharedCollection, 
      salarie.manager
    );

    this.departementsSharedCollection = this.departementService.addDepartementToCollectionIfMissing(
      this.departementsSharedCollection,
      salarie.departement
    );
    this.postesSharedCollection = this.posteService.addPosteToCollectionIfMissing(this.postesSharedCollection, salarie.poste);

  }

  protected loadRelationshipsOptions(): void {
    this.departementService
      .query()
      .pipe(map((res: HttpResponse<IDepartement[]>) => res.body ?? []))
      .pipe(
        map((departements: IDepartement[]) =>
          this.departementService.addDepartementToCollectionIfMissing(departements, this.editForm.get('departement')!.value)
        )
      )
      .subscribe((departements: IDepartement[]) => (this.departementsSharedCollection = departements));

    this.posteService
      .query()
      .pipe(map((res: HttpResponse<IPoste[]>) => res.body ?? []))
      .pipe(map((postes: IPoste[]) => this.posteService.addPosteToCollectionIfMissing(postes, this.editForm.get('poste')!.value)))
      .subscribe((postes: IPoste[]) => (this.postesSharedCollection = postes));

    this.salarieService
    .query()
      .pipe(map((res: HttpResponse<ISalarie[]>) => res.body ?? []))
      .pipe(map((salaries: ISalarie[]) => this.salarieService.addSalarieToCollectionIfMissing(salaries, this.editForm.get('salarie')!.value)))
      .subscribe((salaries: ISalarie[]) => (this.salariesSharedCollection = salaries));

  }

  protected createFromForm(): ISalarie {
    return {
      ...new Salarie(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      login: this.editForm.get(['login'])!.value,
      email: this.editForm.get(['email'])!.value,
      manager: this.editForm.get(['manager'])!.value,
      role: this.editForm.get(['role'])!.value,
      actif: this.editForm.get(['actif'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value,
      iDEntreprise: this.editForm.get(['iDEntreprise'])!.value,
      lDAPPath: this.editForm.get(['lDAPPath'])!.value,
      departement: this.editForm.get(['departement'])!.value,
      poste: this.editForm.get(['poste'])!.value,
    };
  }
}
