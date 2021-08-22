import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITypeConge, TypeConge } from '../type-conge.model';
import { TypeCongeService } from '../service/type-conge.service';

@Component({
  selector: 'jhi-type-conge-update',
  templateUrl: './type-conge-update.component.html',
})
export class TypeCongeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [],
  });

  constructor(protected typeCongeService: TypeCongeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeConge }) => {
      this.updateForm(typeConge);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeConge = this.createFromForm();
    if (typeConge.id !== undefined) {
      this.subscribeToSaveResponse(this.typeCongeService.update(typeConge));
    } else {
      this.subscribeToSaveResponse(this.typeCongeService.create(typeConge));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeConge>>): void {
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

  protected updateForm(typeConge: ITypeConge): void {
    this.editForm.patchValue({
      id: typeConge.id,
      nom: typeConge.nom,
    });
  }

  protected createFromForm(): ITypeConge {
    return {
      ...new TypeConge(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
    };
  }
}
